package cc.xfl12345.mybigdata.server.web.model.checker;

import cc.xfl12345.mybigdata.server.common.appconst.JsonSchemaKeyWords;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JsonChecker {
    @Getter
    protected JsonSchema jsonSchema;

    protected ObjectMapper objectMapper;

    protected JsonNode jsonObject;

    public JsonChecker(ObjectMapper objectMapper, JsonSchema jsonSchema) {
        this.jsonSchema = jsonSchema;
        this.objectMapper = objectMapper;
        this.jsonObject = jsonSchema.getSchemaNode();
    }

    public JsonNode getJsonObject() {
        return jsonObject.deepCopy();
    }

    public String getMetaSchema() {
        return jsonObject.get(JsonSchemaKeyWords.SCHEMA.getName()).asText();
    }

    public boolean check(String jsonString) {
        boolean isOK = false;
        try {
            isOK = check(objectMapper.readTree(jsonString));
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return isOK;
    }

    public boolean check(JsonNode obj) {
        return jsonSchema.validate(obj).isEmpty();
    }

    public boolean check(Object obj) {
        return check(objectMapper.valueToTree(obj));
    }
}
