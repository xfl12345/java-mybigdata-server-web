package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.web.plugin.networknt.schema.DraftV202012HyperSchema;
import cc.xfl12345.mybigdata.server.web.plugin.networknt.schema.DraftV202012Links;
import cc.xfl12345.mybigdata.server.web.plugin.networknt.schema.DraftV202012Schema;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonMetaSchema;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class StudyJsonSchema {
    ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        StudyJsonSchema studyJsonSchema = new StudyJsonSchema();
        studyJsonSchema.test();
    }

    private Map<String, String> getUriMappingsFromUrl(URL url) throws IOException {
        HashMap<String, String> map = new HashMap<String, String>();
        for (JsonNode mapping : objectMapper.readTree(url)) {
            map.put(mapping.get("publicURL").asText(),
                "resource:/" + mapping.get("localPath").asText());
        }
        return map;
    }

    public void test() throws IOException {


        URL mappingsURL = Thread.currentThread().getContextClassLoader()
            .getResource("json/conf/json_schema_validator_uri_mapping.json");


        JsonMetaSchema draftV202012HyperSchema = new DraftV202012HyperSchema().getInstance();
        JsonMetaSchema draftV202012Links = new DraftV202012Links().getInstance();
        JsonMetaSchema draftV202012 = new DraftV202012Schema().getInstance();

        JsonSchemaFactory factory = new JsonSchemaFactory.Builder()
            .addMetaSchema(draftV202012)
            .addMetaSchema(draftV202012Links)
            .addMetaSchema(draftV202012HyperSchema)
            .defaultMetaSchemaURI(draftV202012.getUri())
            .addUriMappings(getUriMappingsFromUrl(mappingsURL))
            .build();


        String jsonInString = "{\n" +
            "    \"$schema\": \"https://json-schema.org/draft/2020-12/schema\",\n" +
            "    \"title\": \"mybatis_row_bounds_object\",\n" +
            "    \"description\": \"MyBatis RowBound 对象的字段检验\",\n" +
            "    \"type\": \"object\",\n" +
            "    \"properties\": {\n" +
            "        \"offset\": {\n" +
            "            \"description\": \"从第几行之后开始\",\n" +
            "            \"type\": \"integer\",\n" +
            "            \"minimum\": 0\n" +
            "        },\n" +
            "        \"limit\": {\n" +
            "            \"description\": \"总共多少个\",\n" +
            "            \"type\": \"integer\",\n" +
            "            \"minimum\": 0\n" +
            "        }\n" +
            "    },\n" +
            "    \"required\": [\n" +
            "        \"offset\",\n" +
            "        \"limit\"\n" +
            "    ]\n" +
            "}\n";
        JsonNode jsonNode = objectMapper.readTree(jsonInString);
        JsonSchema jsonSchema = factory.getSchema(URI.create(draftV202012.getUri()));
        Set<ValidationMessage> errors;

        errors = jsonSchema.validate(jsonNode);

        System.out.print("\n".repeat(10));
        System.out.println("#".repeat(60));
        System.out.println(
            JSON.toJSONString(errors, JSONWriter.Feature.PrettyFormat)
        );
        System.out.println("#".repeat(60));
        System.out.print("\n".repeat(10));


        errors = jsonSchema.validate(objectMapper.valueToTree(JSONObject.parseObject(jsonInString)));

        System.out.print("\n".repeat(10));
        System.out.println("#".repeat(60));
        System.out.println(
            JSON.toJSONString(errors, JSONWriter.Feature.PrettyFormat)
        );
        System.out.println("#".repeat(60));
        System.out.print("\n".repeat(10));

    }
}
