package cc.xfl12345.mybigdata.server.initializer;

import cc.xfl12345.mybigdata.server.model.FileURIRelativizeURIUtil;
import cc.xfl12345.mybigdata.server.model.IRelativizeURIUtil;
import cc.xfl12345.mybigdata.server.model.JarFileURIRelativizeURIUtil;
import cc.xfl12345.mybigdata.server.model.checker.OfflineJsonChecker;
import cc.xfl12345.mybigdata.server.pojo.FileCacheInfoBean;
import cc.xfl12345.mybigdata.server.pojo.ResourceCacheMapBean;
import com.alibaba.fastjson.JSONObject;
import com.google.common.jimfs.Handler;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;


public class OfflineJsonSchemaURLInitializer implements InitializingBean {
    protected URI jsonSchemaResourceDirectoryURI = null;
    protected URLStreamHandler ramFileSystemURLStreamHandler = null;
    protected URI rootJsonSchemaFileURI = null;

    @Getter
    protected URL rootJsonSchemaFileURL = null;

    @Getter
    @Setter
    protected ResourceCacheMapBean cacheMapBean;

    @Getter
    @Setter
    protected StandardFileSystemManager fileSystemManager;

    @Getter
    @Setter
    protected FileSystem ramFS;

    @Getter
    @Setter
    protected String jsonSchemaResourceDirectoryRelativePath = "json/schema/";

    @Getter
    @Setter
    protected String jsonSchemaRootFileRelativePath = "json/schema/spec/2020-12/schema.json";

    protected void copyAndModify(FileObject src, IRelativizeURIUtil relativizeURIUtil, Map<String, String> injectValues) throws IOException, URISyntaxException {
        URI currFileURI = src.getURI();
        URL currFileURL = src.getURL();
        String currFileUrlInString = currFileURL.toString();
        String relativePath = '/' + jsonSchemaResourceDirectoryRelativePath
            + relativizeURIUtil.getRelativizeURI(jsonSchemaResourceDirectoryURI, currFileURI);
        Path pathInRamFS = ramFS.getPath(relativePath);
        if (src.isFolder()) {
//            throw new IllegalArgumentException("Except file but get directory.");
            // 同步创建目录
            Files.createDirectories(pathInRamFS);
        } else {
            // 先创建目录，再拷贝文件
            Files.createDirectories(pathInRamFS.getParent());
            // 读 JSON
            InputStream inputStream = currFileURL.openStream();
            String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            inputStream.close();
            // 修改 JSON
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            jsonObject.putAll(injectValues);
            // 字符串 转 输入流
            inputStream = IOUtils.toInputStream(jsonObject.toJSONString(), StandardCharsets.UTF_8);
            // 写文件
            Files.copy(inputStream, pathInRamFS, StandardCopyOption.REPLACE_EXISTING);
            inputStream.close();
        }

        // 记录映射关系，方便找到源文件
        FileCacheInfoBean cacheInfo = cacheMapBean.putIfAbsent(currFileUrlInString, new FileCacheInfoBean());
        if (cacheInfo == null) {
            cacheInfo = cacheMapBean.get(currFileUrlInString);
        }
        cacheInfo.setOriginURL(currFileURL);
        cacheInfo.addCacheURL(new URL(null, pathInRamFS.toUri().toString(), ramFileSystemURLStreamHandler));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FileObject jsonSchemaResourceDirectory = fileSystemManager.resolveFile(
            "res:/" + jsonSchemaResourceDirectoryRelativePath
        );
        jsonSchemaResourceDirectoryURI = jsonSchemaResourceDirectory.getURI();

        FileObject jsonSchemaRootFileSource = fileSystemManager.resolveFile(
            "res:/" + jsonSchemaRootFileRelativePath
        );

        IRelativizeURIUtil relativizeURIUtil;
        // 如果 SpringBoot 以 JAR 形式运行，这意味着 系统ClassLoader 可能没有加载 Jimfs
        // 也意味着 java.nio 那些东西部分无法使用。
        if ("jar".equals(jsonSchemaResourceDirectory.getURL().getProtocol())) {
            relativizeURIUtil = new JarFileURIRelativizeURIUtil();
            try {
                FileSystems.getFileSystem(ramFS.getPath("/").toUri());
            } catch (ProviderNotFoundException | FileSystemNotFoundException e) {
                ramFileSystemURLStreamHandler = new Handler();
            }
        } else {
            relativizeURIUtil = new FileURIRelativizeURIUtil();
        }

        // 创建 根 JSON Schema 文件 URL
        rootJsonSchemaFileURI = ramFS.getPath('/' + jsonSchemaRootFileRelativePath).toUri();
        rootJsonSchemaFileURL = new URL(
            null, rootJsonSchemaFileURI.toString(), ramFileSystemURLStreamHandler
        );
        HashMap<String, String> injectValues = new HashMap<>();
        injectValues.put(
            OfflineJsonChecker.KEY_WORD_SCHEMA,
            rootJsonSchemaFileURL.toString()
        );
        copyAndModify(jsonSchemaRootFileSource, relativizeURIUtil, injectValues);


        jsonSchemaResourceDirectoryURI = jsonSchemaResourceDirectory.getURI();
        Queue<FileObject> queue = new ConcurrentLinkedDeque<>();
        queue.add(jsonSchemaResourceDirectory);
        while (!queue.isEmpty()) {
            FileObject currFile = queue.remove();
            if (currFile.isFolder()) {
                copyAndModify(jsonSchemaRootFileSource, relativizeURIUtil, injectValues);
                // 加入迭代
                queue.addAll(Arrays.stream(currFile.getChildren()).parallel().toList());
            } else {
                copyAndModify(currFile, relativizeURIUtil, injectValues);
            }
        }
    }
}
