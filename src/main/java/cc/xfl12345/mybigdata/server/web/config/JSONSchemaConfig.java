package cc.xfl12345.mybigdata.server.web.config;

import cc.xfl12345.mybigdata.server.common.data.source.pojo.CommonMbdId;
import cc.xfl12345.mybigdata.server.common.pojo.MbdId;
import cc.xfl12345.mybigdata.server.web.model.checker.JsonChecker;
import cc.xfl12345.mybigdata.server.web.plugin.networknt.schema.DraftV202012HyperSchema;
import cc.xfl12345.mybigdata.server.web.plugin.networknt.schema.DraftV202012Links;
import cc.xfl12345.mybigdata.server.web.plugin.networknt.schema.DraftV202012Schema;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchemaFactory;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

@Configuration
public class JSONSchemaConfig {
    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        SimpleModule simpleModule = new SimpleModule();

        simpleModule.addSerializer(MbdId.class, ToStringSerializer.instance);
        simpleModule.addDeserializer(MbdId.class, new JsonDeserializer<>() {
            @Override
            public MbdId<?> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                return new CommonMbdId(parser.getText());
            }
        });
        simpleModule.addSerializer(CommonMbdId.class, ToStringSerializer.instance);
        simpleModule.addDeserializer(CommonMbdId.class, new JsonDeserializer<>() {
            @Override
            public CommonMbdId deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                return new CommonMbdId(parser.getText());
            }
        });

        simpleModule.addSerializer(ObjectId.class, ToStringSerializer.instance);
        simpleModule.addDeserializer(ObjectId.class, new JsonDeserializer<>() {
            @Override
            public ObjectId deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                return new ObjectId(parser.getText());
            }
        });

        return new Jackson2ObjectMapperBuilder().modules(simpleModule);
    }

    public static URI createJsonSchemFileURI(String resourcePath) {
        return URI.create("resource:/cc/xfl12345/mybigdata/server/common/" + resourcePath);
    }

    @Bean
    public JsonSchemaFactory jsonSchemaFactory(ObjectMapper mapper) throws IOException {
        URL mappingsURL = Thread.currentThread().getContextClassLoader()
            .getResource("cc/xfl12345/mybigdata/server/common/json/conf/json_schema_validator_uri_mapping.json");

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

        return new JsonSchemaFactory.Builder()
            .objectMapper(mapper)
            .addMetaSchema(draftV202012)
            .addMetaSchema(draftV202012Links)
            .addMetaSchema(draftV202012HyperSchema)
            .defaultMetaSchemaURI(draftV202012HyperSchema.getUri())
            .addUriMappings(mapping)
            .build();
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
    public SchemaGeneratorConfigBuilder schemaGeneratorConfigBuilder() {
        return new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
    }

    @Bean
    public SchemaGenerator schemaGenerator(SchemaGeneratorConfigBuilder schemaGeneratorConfigBuilder) {
        return new SchemaGenerator(schemaGeneratorConfigBuilder.build());
    }
}
