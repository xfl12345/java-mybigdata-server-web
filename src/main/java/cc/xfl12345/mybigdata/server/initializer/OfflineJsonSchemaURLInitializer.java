package cc.xfl12345.mybigdata.server.initializer;

import cc.xfl12345.mybigdata.server.model.FileURIRelativizeURIUtil;
import cc.xfl12345.mybigdata.server.model.IRelativizeURIUtil;
import cc.xfl12345.mybigdata.server.model.JarFileURIRelativizeURIUtil;
import cc.xfl12345.mybigdata.server.model.checker.OfflineJsonChecker;
import cc.xfl12345.mybigdata.server.pojo.FileCacheInfoBean;
import cc.xfl12345.mybigdata.server.pojo.ResourceCacheMapBean;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ram.RamFileObject;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;


public class OfflineJsonSchemaURLInitializer implements InitializingBean {
    protected URI jsonSchemaResourceDirectoryURI = null;
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

        RamFileObject fileInRamFS = (RamFileObject) fileSystemManager.resolveFile("ram:/" + relativePath);
        if (src.isFolder()) {
            // 同步创建目录
            fileInRamFS.copyFrom(src, Selectors.SELECT_SELF);
        } else {
            // 先创建目录，再拷贝文件
            fileInRamFS.getParent().createFolder();
            // 读 JSON
            InputStream inputStream = currFileURL.openStream();
            String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            inputStream.close();
            // 修改 JSON
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            jsonObject.putAll(injectValues);
            // 写文件
            OutputStream outputStream = fileInRamFS.getOutputStream();
            outputStream.write(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        }

        // 记录映射关系，方便找到源文件
        FileCacheInfoBean cacheInfo = cacheMapBean.putIfAbsent(currFileUrlInString, new FileCacheInfoBean());
        if (cacheInfo == null) {
            cacheInfo = cacheMapBean.get(currFileUrlInString);
        }
        cacheInfo.setOriginURL(currFileURL);
        cacheInfo.addCacheURL(fileInRamFS.getURL());
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
        if ("jar".equals(jsonSchemaResourceDirectory.getURL().getProtocol())) {
            relativizeURIUtil = new JarFileURIRelativizeURIUtil();
        } else {
            relativizeURIUtil = new FileURIRelativizeURIUtil();
        }

        // 创建 根 JSON Schema 文件 URL
        rootJsonSchemaFileURL = fileSystemManager.resolveFile("ram:/" + jsonSchemaRootFileRelativePath).getURL();
        rootJsonSchemaFileURI = rootJsonSchemaFileURL.toURI();
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
                copyAndModify(currFile, relativizeURIUtil, injectValues);
                // 加入迭代
                queue.addAll(Arrays.stream(currFile.getChildren()).parallel().toList());
            } else {
                copyAndModify(currFile, relativizeURIUtil, injectValues);
            }
        }
    }
}
