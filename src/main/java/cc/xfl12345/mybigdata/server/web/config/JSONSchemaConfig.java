package cc.xfl12345.mybigdata.server.web.config;

import cc.xfl12345.mybigdata.server.web.model.checker.JsonChecker;
import cc.xfl12345.mybigdata.server.web.plugin.networknt.schema.DraftV202012HyperSchema;
import cc.xfl12345.mybigdata.server.web.plugin.networknt.schema.DraftV202012Links;
import cc.xfl12345.mybigdata.server.web.plugin.networknt.schema.DraftV202012Schema;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchemaFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.AotProxyHint;
import org.springframework.nativex.hint.ProxyBits;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

@Configuration(proxyBeanMethods = false)
@AotProxyHint(targetClass = JSONSchemaConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
public class JSONSchemaConfig {

    public static URI createJsonSchemFileURI(String resourcePath) {
        return URI.create("resource:/" + resourcePath);
    }


    @Bean
    public ObjectMapper jsonObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JsonSchemaFactory jsonSchemaFactory(ObjectMapper mapper) throws IOException {
        URL mappingsURL = Thread.currentThread().getContextClassLoader()
            .getResource("json/conf/json_schema_validator_uri_mapping.json");

        HashMap<String, String> mapping = new HashMap<String, String>();
        for (JsonNode jsonNode : mapper.readTree(mappingsURL)) {
            mapping.put(
                jsonNode.get("publicURL").asText(),
                createJsonSchemFileURI(jsonNode.get("localPath").asText()).toString()
            );
        }


        JsonMetaSchema draftV202012HyperSchema = new DraftV202012HyperSchema().getInstance();
        JsonMetaSchema draftV202012Links = new DraftV202012Links().getInstance();
        JsonMetaSchema draftV202012 = new DraftV202012Schema().getInstance();

        JsonSchemaFactory factory = new JsonSchemaFactory.Builder()
            .addMetaSchema(draftV202012)
            .addMetaSchema(draftV202012Links)
            .addMetaSchema(draftV202012HyperSchema)
            .defaultMetaSchemaURI(draftV202012HyperSchema.getUri())
            // .defaultMetaSchemaURI(draftV202012.getUri())
            .addUriMappings(mapping)
            .build();

        return factory;
    }

    @Bean
    public JsonChecker jsonSchemaChecker(ObjectMapper jsonObjectMapper, JsonSchemaFactory jsonSchemaFactory) {
        return new JsonChecker(
            jsonObjectMapper,
            jsonSchemaFactory.getSchema(
                URI.create(new DraftV202012HyperSchema().getInstance().getUri())
            )
        );
    }

    @Bean
    public JsonChecker baseRequestObjectChecker(ObjectMapper jsonObjectMapper, JsonSchemaFactory jsonSchemaFactory) {
        return new JsonChecker(
            jsonObjectMapper,
            jsonSchemaFactory.getSchema(
                createJsonSchemFileURI("json/schema/base_request_object.json")
            )
        );
    }

    @Bean
    public JsonChecker mybatisRowBoundsObjectChecker(ObjectMapper jsonObjectMapper, JsonSchemaFactory jsonSchemaFactory) {
        return new JsonChecker(
            jsonObjectMapper,
            jsonSchemaFactory.getSchema(
                createJsonSchemFileURI("json/schema/mybatis_row_bounds_object.json")
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
