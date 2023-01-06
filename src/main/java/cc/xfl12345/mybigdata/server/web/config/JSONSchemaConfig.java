package cc.xfl12345.mybigdata.server.web.config;

import cc.xfl12345.mybigdata.server.common.json.JsonResourceMapper;
import cc.xfl12345.mybigdata.server.web.model.checker.JsonChecker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchemaFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class JSONSchemaConfig {
    @Getter
    protected JsonResourceMapper jsonResourceMapper;

    @Autowired
    public void setJsonResourceMapper(JsonResourceMapper jsonResourceMapper) {
        this.jsonResourceMapper = jsonResourceMapper;
    }

    @Bean
    public JsonChecker jsonSchemaChecker(
        ObjectMapper jsonObjectMapper,
        JsonSchemaFactory jsonSchemaFactory,
        @Qualifier("jsonSchemaDraftV202012") JsonMetaSchema jsonSchemaDraftV202012) {
        return new JsonChecker(
            jsonObjectMapper,
            jsonSchemaFactory.getSchema(
                URI.create(jsonSchemaDraftV202012.getUri())
            )
        );
    }

    @Bean
    public JsonChecker baseRequestObjectChecker(
        ObjectMapper jsonObjectMapper,
        JsonSchemaFactory jsonSchemaFactory,
        JsonResourceMapper jsonResourceMapper) {
        return new JsonChecker(
            jsonObjectMapper,
            jsonSchemaFactory.getSchema(
                jsonResourceMapper.createJsonSchemFileURI("schema/base_request_object.json")
            )
        );
    }

    @Bean
    public SchemaGeneratorConfigBuilder schemaGeneratorConfigBuilder() {
        return new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
    }

    @Bean
    public SchemaGenerator schemaGenerator(SchemaGeneratorConfigBuilder schemaGeneratorConfigBuilder) {
        return new SchemaGenerator(schemaGeneratorConfigBuilder.build());
    }
}
