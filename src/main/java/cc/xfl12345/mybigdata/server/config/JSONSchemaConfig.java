package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.initializer.OfflineJsonSchemaURLInitializer;
import cc.xfl12345.mybigdata.server.model.checker.OfflineJsonChecker;
import cc.xfl12345.mybigdata.server.plugin.jsonschemafriend.ApacheURLBasedCacheLoader;
import cc.xfl12345.mybigdata.server.pojo.ResourceCacheMapBean;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.Validator;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

@Configuration
public class JSONSchemaConfig {

    @Autowired
    protected AppConfig appConfig;

    @Bean(name = "offlineJsonSchemaURLInitializer")
    @Scope(value = "singleton")
    public OfflineJsonSchemaURLInitializer getOfflineJsonSchemaURLInitializer() throws IOException {
        ResourceCacheMapBean cacheMapBean = appConfig.getResourceCacheBean();
        StandardFileSystemManager fileSystemManager = appConfig.getStandardFileSystemManager();

        OfflineJsonSchemaURLInitializer initializer = new OfflineJsonSchemaURLInitializer();
        initializer.setCacheMapBean(cacheMapBean);
        initializer.setFileSystemManager(fileSystemManager);

        return initializer;
    }

    @Bean(name = "apacheURLBasedCacheLoader")
    @Scope(value = "singleton")
    public ApacheURLBasedCacheLoader getApacheURLBasedCacheLoader() throws IOException {
        return new ApacheURLBasedCacheLoader(appConfig.getStandardFileSystemManager());
    }


    @Bean(name = "jsonSchemaStore")
    @Scope(value = "singleton")
    public SchemaStore getSchemaStore() throws IOException {
        return new SchemaStore(getApacheURLBasedCacheLoader());
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
        URL url = appConfig.getStandardFileSystemManager()
            .resolveFile("ram:/" + "json/schema/base_request_object.json").getURL();
        return new OfflineJsonChecker(
            getSchemaStore(),
            getValidator(),
            url,
            getJsonSchemaChecker()
        );
    }

    @Bean(name = "mybatisRowBoundsObjectChecker")
    public OfflineJsonChecker getMybatisRowBoundsObjectChecker() throws GenerationException, IOException, URISyntaxException {
        URL url = appConfig.getStandardFileSystemManager()
            .resolveFile("ram:/" + "json/schema/mybatis_row_bounds_object.json").getURL();
        return new OfflineJsonChecker(
            getSchemaStore(),
            getValidator(),
            url,
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
