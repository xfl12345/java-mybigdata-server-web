package cc.xfl12345.mybigdata.server.model.utility.check;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.uuid.Generators;
import lombok.extern.slf4j.Slf4j;
import net.jimblackler.jsonschemafriend.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class OfflineJsonChecker {
    protected Validator validator;
    protected File file;
    protected JSONObject jsonObject;
    protected Schema schema;

    protected final static String KEY_WORD_SCHEMA = "$schema";
    protected final static String KEY_WORD_ID = "$id";
    protected final static String KEY_WORD_PROPERTIES = "properties";

    public OfflineJsonChecker(
        SchemaStore schemaStore,
        Validator validator,
        File jsonSchemaFile,
        OfflineJsonChecker parent) throws GenerationException, IOException {
        this.validator = validator;
        // Load the schema.
        file = jsonSchemaFile;
        jsonObject = getJSONObjectFromFile(file);
        if (parent == null) {
            modifyByFileURI(jsonObject, file, file);
        } else {
            modifyByFileURI(jsonObject, file, parent.file);
        }
        schema = schemaStore.loadSchema(jsonObject);
    }

    public OfflineJsonChecker(
        SchemaStore schemaStore,
        Validator validator,
        String json,
        OfflineJsonChecker parent) throws GenerationException {
        this.validator = validator;
        // Load the schema.
        jsonObject = JSONObject.parseObject(json, Feature.OrderedField);
        if (parent == null) {
            if (jsonObject.containsKey(KEY_WORD_SCHEMA)) {
                file = new File(URI.create(
                    jsonObject.getString(KEY_WORD_SCHEMA)
                ));
            } else {
                String generatePath = getClass().getClassLoader()
                    .getResource("").getPath()
                    + "ram/root_json_schema.json";
                log.info("Generate virtual path=" + generatePath + " <---> " + jsonObject.getString("title"));
                file = new File(generatePath);
            }
            modifyByFileURI(jsonObject, file, file);
        } else {
            String generatePath = getClass().getClassLoader()
                .getResource("").getPath()
                + "ram/"
                + Generators.timeBasedGenerator().generate().toString()
                + ".json";
            log.info("Generate virtual path=" + generatePath + " <---> " + jsonObject.getString("title"));
            file = new File(generatePath);
            modifyByFileURI(jsonObject, file, parent.file);
        }
        schema = schemaStore.loadSchema(jsonObject);
    }

    public static JSONObject getJSONObjectFromFile(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        JSONObject jsonObject = JSONObject.parseObject(
            inputStream,
            StandardCharsets.UTF_8,
            JSONObject.class,
            Feature.OrderedField
        );
        inputStream.close();
        return jsonObject;
    }

    public static void modifyByFileURI(JSONObject jsonObject, File file, File rootSchema) {
        jsonObject.remove("$schema");
        jsonObject.put("$schema", rootSchema.toURI().toString());
        jsonObject.remove("$id");
        jsonObject.put("$id", file.toURI().toString());
    }

    public File getFile() {
        return file;
    }

    public JSONObject getJsonObject() {
        return jsonObject.clone();
    }

    public String getParentJsonSchema() {
        return jsonObject.getString(KEY_WORD_SCHEMA);
    }

    public boolean getCheckResultAsBoolean(String jsonString) {
        boolean isOK = false;
        try {
            isOK = getCheckResultAsBoolean(JSON.parseObject(jsonString));
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
        validator.validate(schema, JSON.parseObject(jsonString));
    }

    public void check(Object obj) throws ValidationException {
        validator.validate(schema, obj);
    }

    public void setJsonObjectPropertiesOrder(List<String> list) {
        JSONObject properties = jsonObject.getJSONObject(KEY_WORD_PROPERTIES);
        JSONObject orderedProperties = new JSONObject(true);
        for (String item : list) {
            JSONObject property = properties.getJSONObject(item);
            if (property != null) {
                orderedProperties.put(item, property);
            }
        }
        jsonObject.replace(KEY_WORD_PROPERTIES, orderedProperties);
    }
}
