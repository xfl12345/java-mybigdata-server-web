package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.common.json.JsonResourceMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.*;

import java.io.IOException;
import java.net.URI;
import java.util.Set;


public class StudyJsonSchema {

    public static void main(String[] args) throws IOException {
        StudyJsonSchema studyJsonSchema = new StudyJsonSchema();
        studyJsonSchema.test();
    }

    public void test() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonMetaSchema draftV202012 = JsonSchemaFactory.checkVersion(SpecVersion.VersionFlag.V202012).getInstance();
        JsonResourceMapper jsonResourceMapper = new JsonResourceMapper();
        jsonResourceMapper.setObjectMapper(objectMapper);
        jsonResourceMapper.init();

        JsonSchemaFactory factory = new JsonSchemaFactory.Builder()
            .addUriMappings(jsonResourceMapper.getUriMappings())
            .addMetaSchema(draftV202012)
            .defaultMetaSchemaURI(draftV202012.getUri())
            .build();


        String jsonInString = """
            {
                "$schema": "https://json-schema.org/draft/2020-12/schema",
                "title": "mybatis_row_bounds_object",
                "description": "MyBatis RowBound 对象的字段检验",
                "type": "object",
                "properties": {
                    "offset": {
                        "description": "从第几行之后开始",
                        "type": "integer",
                        "minimum": 0
                    },
                    "limit": {
                        "description": "总共多少个",
                        "type": "integer",
                        "minimum": 0
                    },
                    "qqq": {
                        "type": "object",
                        "properties": {
                            "vvv": {
                                "type": "string"
                            }
                        },
                        "required": ["vvv"]
                    }
                },
                "required": [
                    "offset",
                    "limit"
                ]
            }
            """;
        JsonNode jsonNode = objectMapper.readTree(jsonInString);
        JsonSchema jsonSchema = factory.getSchema(jsonNode);
        JsonSchema draftV202012JsonSchema = factory.getSchema(URI.create(draftV202012.getUri()));
        Set<ValidationMessage> errors;

        errors = draftV202012JsonSchema.validate(jsonNode);

        System.out.print("\n".repeat(10));
        System.out.println("#".repeat(60));
        System.out.println(
            objectMapper.valueToTree(errors).toPrettyString()
        );
        System.out.println("#".repeat(60));
        System.out.print("\n".repeat(10));


        errors = draftV202012JsonSchema.validate(objectMapper.readTree(jsonInString));

        System.out.print("\n".repeat(10));
        System.out.println("#".repeat(60));
        System.out.println(
            objectMapper.valueToTree(errors).toPrettyString()
        );
        System.out.println("#".repeat(60));
        System.out.print("\n".repeat(10));


        System.out.print("\n".repeat(10));
        System.out.println("#".repeat(60));
        System.out.println(
            objectMapper.valueToTree(jsonSchema.getSchemaNode().at("/title")).toPrettyString()
        );
        System.out.println("#".repeat(60));
        System.out.print("\n".repeat(10));


        System.out.print("\n".repeat(10));
        System.out.println("#".repeat(60));
        System.out.println(
            objectMapper.valueToTree(
                factory.getSchema(jsonSchema.getRefSchemaNode("#/properties/qqq"))
                    .validate(objectMapper.readTree("{\"vvv\": true}"))
            ).toPrettyString()
        );
        System.out.println("#".repeat(60));
        System.out.print("\n".repeat(10));


    }
}
