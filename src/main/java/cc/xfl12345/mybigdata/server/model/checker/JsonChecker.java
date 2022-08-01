package cc.xfl12345.mybigdata.server.model.checker;

import cc.xfl12345.mybigdata.server.appconst.JsonSchemaKeyWords;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;


@Slf4j
public class JsonChecker {

    @Getter
    protected JsonSchema jsonSchema;

    protected ObjectMapper objectMapper;

    protected JSONObject jsonObject;

    public JsonChecker(ObjectMapper objectMapper, JsonSchema jsonSchema) {
        this.jsonSchema = jsonSchema;
        this.objectMapper = objectMapper;
        this.jsonObject = JSONObject.parseObject(jsonSchema.getSchemaNode().toString());
    }

    public JSONObject getJsonObject() {
        return jsonObject.clone();
    }

    public String getMetaSchema() {
        return jsonObject.getString(JsonSchemaKeyWords.SCHEMA.getName());
    }

    public boolean getCheckResultAsBoolean(String jsonString) {
        boolean isOK = false;
        try {
            Set<ValidationMessage> errors = jsonSchema.validate(objectMapper.readTree(jsonString));
            isOK = errors.isEmpty();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return isOK;
    }

    public boolean getCheckResultAsBoolean(Object obj) {
        boolean isOK = false;
        try {
            Set<ValidationMessage> errors = jsonSchema.validate(objectMapper.valueToTree(obj));
            isOK = errors.isEmpty();
        } catch (Exception e) {
            log.debug(e.getMessage());
        }

        return isOK;
    }
}
