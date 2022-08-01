package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.checker.JsonChecker;
import cc.xfl12345.mybigdata.server.plugin.networknt.schema.DraftV202012HyperSchema;
import cc.xfl12345.mybigdata.server.plugin.networknt.schema.DraftV202012Links;
import cc.xfl12345.mybigdata.server.plugin.networknt.schema.DraftV202012Schema;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchemaFactory;
import lombok.Getter;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

@Configuration
public class JSONSchemaConfig {
    @Getter
    protected StandardFileSystemManager standardFileSystemManager;

    @Autowired
    public void setStandardFileSystemManager(StandardFileSystemManager standardFileSystemManager) {
        this.standardFileSystemManager = standardFileSystemManager;
    }

    @Bean(name = "jsonObjectMapper")
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }


    @Bean(name = "jsonSchemaFactory")
    public JsonSchemaFactory getJsonSchemaFactory() throws IOException {
        ObjectMapper mapper = getObjectMapper();

        URL mappingsURL = Thread.currentThread().getContextClassLoader()
            .getResource("json/conf/json_schema_validator_uri_mapping.json");

        HashMap<String, String> mapping = new HashMap<String, String>();
        for (JsonNode jsonNode : mapper.readTree(mappingsURL)) {
            mapping.put(jsonNode.get("publicURL").asText(),
                "resource:/" + jsonNode.get("localPath").asText());
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

    @Bean(name = "jsonSchemaChecker")
    public JsonChecker getJsonSchemaChecker() throws IOException {
        return new JsonChecker(
            getObjectMapper(),
            getJsonSchemaFactory().getSchema(URI.create(
                new DraftV202012HyperSchema().getInstance().getUri()
            ))
        );
    }

    @Bean(name = "baseRequestObjectChecker")
    public JsonChecker getBaseRequestObjectChecker() throws IOException {
        return new JsonChecker(
            getObjectMapper(),
            getJsonSchemaFactory().getSchema(URI.create("resource:/" + "json/schema/base_request_object.json"))
        );
    }

    @Bean(name = "mybatisRowBoundsObjectChecker")
    public JsonChecker getMybatisRowBoundsObjectChecker() throws IOException {
        return new JsonChecker(
            getObjectMapper(),
            getJsonSchemaFactory().getSchema(URI.create("resource:/" + "json/schema/mybatis_row_bounds_object.json"))
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
