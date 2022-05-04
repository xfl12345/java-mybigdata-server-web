package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.checker.OfflineJsonChecker;
import cc.xfl12345.mybigdata.server.pojo.FileCacheInfoBean;
import cc.xfl12345.mybigdata.server.pojo.ResourceCacheMapBean;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import lombok.extern.slf4j.Slf4j;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.Validator;
import org.apache.commons.io.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.file.spi.FileSystemProviders;
import org.apache.commons.vfs2.FileDepthSelector;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@Configuration
@Slf4j
public class JSONSchemaConfig {

    @Autowired
    protected AppConfig appConfig;

    @Bean(name = "jsonSchemaStore")
    @Scope(value = "singleton")
    public SchemaStore getSchemaStore() {
        return new SchemaStore();
    }

    @Bean(name = "jsonValidator")
    public Validator getValidator() {
        return new Validator();
    }

    @Bean(name = "jsonSchemaChecker")
    public OfflineJsonChecker getJsonSchemaChecker() throws GenerationException, IOException, URISyntaxException {
        ResourceCacheMapBean cacheMapBean = appConfig.getResourceCacheBean();
        StandardFileSystemManager fileSystemManager = appConfig.getStandardFileSystemManager();
        FileSystem ramFS = appConfig.getJimFS();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        log.info(Arrays.toString(FileSystemProvider.installedProviders().toArray()));


        String jsonSchemaDirRelativePath = "json/schema/";
        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver(classLoader);
        Resource[] resources = resourceResolver.getResources(jsonSchemaDirRelativePath + "**");

        FileObject jsonSchemaFileSource = fileSystemManager.resolveFile(
            "res:/" + jsonSchemaDirRelativePath
        );

        URI jsonSchemaFileSourceURI = jsonSchemaFileSource.getURI();
        Queue<FileObject> queue = new ConcurrentLinkedDeque<>();
        queue.add(jsonSchemaFileSource);
        while (!queue.isEmpty()) {
            FileObject currFile = queue.remove();
            URI currFileURI = currFile.getURI();
            URL currFileURL = currFile.getURL();
            String currFileUrlInString = currFileURL.toString();
//            Path currFilePath = currFile.getPath();

            String relativePath = '/' + jsonSchemaDirRelativePath
                + jsonSchemaFileSourceURI.relativize(currFileURI).getPath();
            Path pathInRamFS = ramFS.getPath(relativePath);
            if(currFile.isFolder()) {
                // 同步创建目录
                Files.createDirectories(pathInRamFS);
                // 加入迭代
                queue.addAll(Arrays.stream(currFile.getChildren()).parallel().toList());
            } else {
                // 先创建目录
//                Path dstPath = Files.createDirectories(pathInRamFS.getParent());
                // 再拷贝文件
                InputStream inputStream = currFileURL.openStream();
                Files.copy(inputStream, pathInRamFS, StandardCopyOption.ATOMIC_MOVE);
                inputStream.close();
            }
            // 记录映射关系，方便找到源文件
            FileCacheInfoBean cacheInfo = cacheMapBean.putIfAbsent(currFileUrlInString, new FileCacheInfoBean());
            if(cacheInfo == null) {
                cacheInfo = cacheMapBean.get(currFileUrlInString);
            }
            cacheInfo.setOriginURL(currFileURL);
            cacheInfo.addCacheURL(pathInRamFS.toUri().toURL());
        }

        URL jsonSchemaSpecFileURL = Resources.getResourceURL("json/schema/spec/2020-12/schema.json");

        return new OfflineJsonChecker(
            getSchemaStore(),
            getValidator(),
            jsonSchemaSpecFileURL,
            null
        );
    }

//    @Bean(name = "jsonSchemaChecker")
//    public OfflineJsonChecker getJsonSchemaChecker() throws GenerationException, IOException, URISyntaxException {
//        ResourceCacheMapBean cacheMapBean = appConfig.getResourceCacheBean();
//        StandardFileSystemManager fileSystemManager = appConfig.getStandardFileSystemManager();
//        FileSystem ramFS = appConfig.getJimFS();
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//
//
//        String jsonSchemaDirRelativePath = "json/schema/";
////        tempURL.toString().substring(0, tempURL.toString().indexOf(jsonSchemaDirRelativePath))
//        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver(classLoader);
//        Resource[] resources = resourceResolver.getResources(jsonSchemaDirRelativePath + "**");
//
////        FileSystem sourceFileSystem = FileSystems.newFileSystem(
////            Paths.get(Objects.requireNonNull(classLoader.getResource(jsonSchemaDirRelativePath)).toURI())
////        );
//
//
////        FileObject ramFile = fileSystemManager.resolveFile("ram:/" + jsonSchemaDirRelativePath);
//        FileObject jsonSchemaFileSource = fileSystemManager.resolveFile(
//            "res:/" + jsonSchemaDirRelativePath
//        );
//        URI jsonSchemaFileSourceURI = jsonSchemaFileSource.getURI();
//        Queue<FileObject> queue = new ConcurrentLinkedDeque<>();
//        queue.add(jsonSchemaFileSource);
//        while (!queue.isEmpty()) {
//            FileObject currFile = queue.remove();
//            URI currFileURI = currFile.getURI();
//            URL currFileURL = currFile.getURL();
//            String currFileUrlInString = currFileURL.toString();
//            Path currFilePath = currFile.getPath();
//            String relativePath = '/' + jsonSchemaDirRelativePath
//                + jsonSchemaFileSourceURI.relativize(currFileURI).getPath();
//            Path pathInRamFS = ramFS.getPath(relativePath);
//            if(currFile.isFolder()) {
//                // 同步创建目录
//                Files.createDirectories(pathInRamFS);
//                // 加入迭代
//                queue.addAll(Arrays.stream(currFile.getChildren()).parallel().toList());
//            } else {
//                // 先创建目录
////                Path dstPath = Files.createDirectories(pathInRamFS.getParent());
//                // 再拷贝文件
//                Files.copy(currFilePath, pathInRamFS, StandardCopyOption.COPY_ATTRIBUTES);
//            }
//            // 记录映射关系，方便找到源文件
//            FileCacheInfoBean cacheInfo = cacheMapBean.putIfAbsent(currFileUrlInString, new FileCacheInfoBean());
//            if(cacheInfo == null) {
//                cacheInfo = cacheMapBean.get(currFileUrlInString);
//            }
//            cacheInfo.setOriginURL(currFileURL);
//            cacheInfo.addCacheURL(pathInRamFS.toUri().toURL());
//        }
//
//        URL jsonSchemaSpecFileURL = Resources.getResourceURL("json/schema/spec/2020-12/schema.json");
//
//        return new OfflineJsonChecker(
//            getSchemaStore(),
//            getValidator(),
//            jsonSchemaSpecFileURL,
//            null
//        );
//    }

    @Bean(name = "baseRequestObjectChecker")
    public OfflineJsonChecker getBaseRequestObjectChecker() throws GenerationException, IOException, URISyntaxException {
        return new OfflineJsonChecker(
            getSchemaStore(),
            getValidator(),
            Resources.getResourceURL("json/schema/base_request_object.json"),
            getJsonSchemaChecker()
        );
    }

    @Bean(name = "mybatisRowBoundsObjectChecker")
    public OfflineJsonChecker getMybatisRowBoundsObjectChecker() throws GenerationException, IOException, URISyntaxException {
        return new OfflineJsonChecker(
            getSchemaStore(),
            getValidator(),
            Resources.getResourceURL("json/schema/mybatis_row_bounds_object.json"),
            getJsonSchemaChecker()
        );
    }

    @Bean(name = "schemaGeneratorConfigBuilder")
    public SchemaGeneratorConfigBuilder getSchemaGeneratorConfigBuilder() {
        return new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
    }

    @Bean(name = "schemaGenerator")
    public SchemaGenerator getSchemaGenerator() {
        return new SchemaGenerator(getSchemaGeneratorConfigBuilder().build());
    }
}
