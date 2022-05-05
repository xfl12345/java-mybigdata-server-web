package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.initializer.OfflineJsonSchemaURLInitializer;
import cc.xfl12345.mybigdata.server.model.FileURIRelativizeURIUtil;
import cc.xfl12345.mybigdata.server.model.IRelativizeURIUtil;
import cc.xfl12345.mybigdata.server.model.JarFileURIRelativizeURIUtil;
import cc.xfl12345.mybigdata.server.model.checker.OfflineJsonChecker;
import cc.xfl12345.mybigdata.server.pojo.FileCacheInfoBean;
import cc.xfl12345.mybigdata.server.pojo.ResourceCacheMapBean;
import com.alibaba.fastjson.JSONObject;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.google.common.jimfs.Handler;
import lombok.extern.slf4j.Slf4j;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.Validator;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@Configuration
@Slf4j
public class JSONSchemaConfig {

    @Autowired
    protected AppConfig appConfig;

    @Bean(name = "offlineJsonSchemaURLInitializer")
    @Scope(value = "singleton")
    public OfflineJsonSchemaURLInitializer getOfflineJsonSchemaURLInitializer() throws org.apache.commons.vfs2.FileSystemException {
        log.info(Arrays.toString(FileSystemProvider.installedProviders().toArray()));
        ResourceCacheMapBean cacheMapBean = appConfig.getResourceCacheBean();
        StandardFileSystemManager fileSystemManager = appConfig.getStandardFileSystemManager();
        FileSystem ramFS = appConfig.getJimFS();

        OfflineJsonSchemaURLInitializer initializer = new OfflineJsonSchemaURLInitializer();
        initializer.setCacheMapBean(cacheMapBean);
        initializer.setFileSystemManager(fileSystemManager);
        initializer.setRamFS(ramFS);

        return initializer;
    }

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
        return new OfflineJsonChecker(
            getSchemaStore(),
            getValidator(),
            getOfflineJsonSchemaURLInitializer().getRootJsonSchemaFileURL(),
            null
        );
    }

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
