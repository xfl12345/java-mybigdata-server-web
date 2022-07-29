package cc.xfl12345.mybigdata.server.model.checker;

import cc.xfl12345.mybigdata.server.appconst.JsonSchemaKeyWords;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.jimblackler.jsonschemafriend.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Slf4j
public class JsonChecker {
    protected Validator validator;

    @Getter
    protected Schema schema;

    @Getter
    protected URL fileURL;

    protected JSONObject jsonObject;

    public JsonChecker(
        SchemaStore schemaStore,
        Validator validator,
        URL jsonSchemaFileURL) throws GenerationException, IOException {
        this.validator = validator;
        // Load the schema.
        fileURL = jsonSchemaFileURL;

        InputStream inputStream = fileURL.openStream();
        jsonObject = JSONObject.parseObject(
            new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
        );
        inputStream.close();

        schema = schemaStore.loadSchema(fileURL);
    }

    public JSONObject getJsonObject() {
        return jsonObject.clone();
    }

    public String getParentJsonSchema() {
        return jsonObject.getString(JsonSchemaKeyWords.SCHEMA.getName());
    }

    public boolean getCheckResultAsBoolean(String jsonString) {
        boolean isOK = false;
        try {
            check(jsonString);
            isOK = true;
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return isOK;
    }

    public boolean getCheckResultAsBoolean(Object obj) {
        boolean isOK = false;
        try {
            check(obj);
            isOK = true;
        } catch (ValidationException e) {
            log.debug(e.getMessage());
        }
        return isOK;
    }

    public void check(String jsonString) throws ValidationException {
        validator.validateJson(schema, jsonString);
    }

    public void check(Object obj) throws ValidationException {
        validator.validate(schema, obj);
    }

    public void setJsonObjectPropertiesOrder(List<String> list) {
        JSONObject properties = jsonObject.getJSONObject(JsonSchemaKeyWords.PROPERTIES.getName());
        JSONObject orderedProperties = new JSONObject();
        for (String item : list) {
            JSONObject property = properties.getJSONObject(item);
            if (property != null) {
                orderedProperties.put(item, property);
            }
        }
        jsonObject.replace(JsonSchemaKeyWords.PROPERTIES.getName(), orderedProperties);
    }
}
