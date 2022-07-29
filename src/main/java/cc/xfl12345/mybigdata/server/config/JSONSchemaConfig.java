package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.initializer.JsonSchemaFileLoader;
import cc.xfl12345.mybigdata.server.model.checker.JsonChecker;
import cc.xfl12345.mybigdata.server.plugin.jsonschemafriend.ApacheURLBasedCacheLoader;
import cc.xfl12345.mybigdata.server.pojo.ResourceCacheMapBean;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import lombok.Getter;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.Validator;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.net.URL;

@Configuration
public class JSONSchemaConfig {
    @Getter
    protected StandardFileSystemManager standardFileSystemManager;

    @Autowired
    public void setStandardFileSystemManager(StandardFileSystemManager standardFileSystemManager) {
        this.standardFileSystemManager = standardFileSystemManager;
    }

    @Getter
    protected ResourceCacheMapBean resourceCacheMapBean;

    @Autowired
    public void setResourceCacheMapBean(ResourceCacheMapBean resourceCacheMapBean) {
        this.resourceCacheMapBean = resourceCacheMapBean;
    }

    @Bean(name = "jsonSchemaFileLoader")
    @Scope(value = "singleton")
    public JsonSchemaFileLoader getJsonSchemaFileLoader() {
        ResourceCacheMapBean cacheMapBean = getResourceCacheMapBean();
        StandardFileSystemManager fileSystemManager = getStandardFileSystemManager();

        JsonSchemaFileLoader loader = new JsonSchemaFileLoader();
        loader.setCacheMapBean(cacheMapBean);
        loader.setFileSystemManager(fileSystemManager);

        return loader;
    }

    @Bean(name = "apacheURLBasedCacheLoader")
    @Scope(value = "singleton")
    public ApacheURLBasedCacheLoader getApacheURLBasedCacheLoader() {
        return new ApacheURLBasedCacheLoader(getStandardFileSystemManager());
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
    public JsonChecker getJsonSchemaChecker() throws GenerationException, IOException {
        URL url = getJsonSchemaFileLoader().getRamfsRootJsonSchemaFileURL();
        return new JsonChecker(getSchemaStore(), getValidator(), url);
    }

    @Bean(name = "baseRequestObjectChecker")
    public JsonChecker getBaseRequestObjectChecker() throws GenerationException, IOException {
        URL url = getStandardFileSystemManager()
            .resolveFile("ram:/" + "json/schema/base_request_object.json").getURL();
        return new JsonChecker(getSchemaStore(), getValidator(), url);
    }

    @Bean(name = "mybatisRowBoundsObjectChecker")
    public JsonChecker getMybatisRowBoundsObjectChecker() throws GenerationException, IOException {
        URL url = getStandardFileSystemManager()
            .resolveFile("ram:/" + "json/schema/mybatis_row_bounds_object.json").getURL();
        return new JsonChecker(getSchemaStore(), getValidator(), url);
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
