package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.checker.OfflineJsonChecker;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.Validator;
import org.apache.commons.vfs2.provider.res.ResourceFileName;
import org.apache.ibatis.io.Resources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.net.URL;

@Configuration
public class JSONSchemaConfig {

    @Bean(name= "jsonSchemaStore")
    @Scope(value = "singleton")
    public SchemaStore getSchemaStore() {
        return new SchemaStore();
    }

    @Bean(name= "jsonValidator")
    public Validator getValidator() {
        return new Validator();
    }

    @Bean(name="jsonSchemaChecker")
    public OfflineJsonChecker getJsonSchemaChecker() throws GenerationException, IOException {
//        URL jsonSchemaSpecFilesRootDir = Resources.getResourceURL();

        URL jsonSchemaSpecFileURL = Resources.getResourceURL("json/schema/spec/2020-12/schema.json");

//        jsonSchemaSpecFileURL.

        return new OfflineJsonChecker(
            getSchemaStore(),
            getValidator(),
            jsonSchemaSpecFileURL,
            null
        );
    }

    @Bean(name="baseRequestObjectChecker")
    public OfflineJsonChecker getBaseRequestObjectChecker() throws GenerationException, IOException {
        return new OfflineJsonChecker(
            getSchemaStore(),
            getValidator(),
            Resources.getResourceURL("json/schema/base_request_object.json"),
            getJsonSchemaChecker()
        );
    }

    @Bean(name="mybatisRowBoundsObjectChecker")
    public OfflineJsonChecker getMybatisRowBoundsObjectChecker() throws GenerationException, IOException {
        return new OfflineJsonChecker(
            getSchemaStore(),
            getValidator(),
            Resources.getResourceURL("json/schema/mybatis_row_bounds_object.json"),
            getJsonSchemaChecker()
        );
    }

    @Bean(name="schemaGeneratorConfigBuilder")
    public SchemaGeneratorConfigBuilder getSchemaGeneratorConfigBuilder() {
        return new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
    }

    @Bean(name="schemaGenerator")
    public SchemaGenerator getSchemaGenerator() {
        return new SchemaGenerator(getSchemaGeneratorConfigBuilder().build());
    }
}
