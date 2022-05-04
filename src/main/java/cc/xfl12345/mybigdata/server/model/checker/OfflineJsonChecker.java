package cc.xfl12345.mybigdata.server.model.checker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.uuid.NoArgGenerator;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import net.jimblackler.jsonschemafriend.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class OfflineJsonChecker {
    protected Validator validator;
    protected URL fileURL;
    protected JSONObject jsonObject;
    protected Schema schema;

    public final static String KEY_WORD_SCHEMA = "$schema";
    public final static String KEY_WORD_ID = "$id";
    public final static String KEY_WORD_PROPERTIES = "properties";

    public OfflineJsonChecker(
        SchemaStore schemaStore,
        Validator validator,
        URL jsonSchemaFileURL,
        OfflineJsonChecker parent) throws GenerationException, IOException {
        this.validator = validator;
        // Load the schema.
        fileURL = jsonSchemaFileURL;
        jsonObject = getJSONObjectFromFile(fileURL);
        if (parent == null) {
            modifyByFileURI(jsonObject, fileURL, fileURL);
        } else {
            modifyByFileURI(jsonObject, fileURL, parent.getFileURL());
        }
        schema = schemaStore.loadSchema(jsonObject);
    }

    public OfflineJsonChecker(
        FileSystem fileSystem,
        NoArgGenerator uuidGenerator,
        SchemaStore schemaStore,
        Validator validator,
        String json,
        OfflineJsonChecker parent) throws GenerationException, IOException {
        this.validator = validator;
        // Load the schema.
        jsonObject = JSONObject.parseObject(json, Feature.OrderedField);
        URL rootSchemaFileURL = parent == null ? null : parent.getFileURL();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (parent == null) {
            if (jsonObject.containsKey(KEY_WORD_SCHEMA)) {
                fileURL = new URL(jsonObject.getString(KEY_WORD_SCHEMA));
            } else {
                json = JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat);
                Path rootPath = fileSystem.getPath("/ram");
                if(!Files.exists(rootPath)) {
                    Files.createDirectory(rootPath);
                }
                Path generatePath = rootPath.resolve("root_json_schema.json");
                Files.write(generatePath, ImmutableList.of(json), StandardCharsets.UTF_8);
                fileURL = generatePath.toUri().toURL();

                log.info("Generate virtual path=" + generatePath + " <---> " + json);
            }
        } else {
            json = JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat);
            Path rootPath = fileSystem.getPath("/ram");
            if(!Files.exists(rootPath)) {
                Files.createDirectory(rootPath);
            }
            Path generatePath = rootPath.resolve(uuidGenerator.generate().toString() + ".json");
            Files.write(generatePath, ImmutableList.of(json), StandardCharsets.UTF_8);
            fileURL = generatePath.toUri().toURL();

            log.info("Generate virtual path=" + generatePath + " <---> " + json);
        }
        modifyByFileURI(jsonObject, fileURL, rootSchemaFileURL);
        schema = schemaStore.loadSchema(jsonObject);
    }

    public static JSONObject getJSONObjectFromFile(URL fileURL) throws IOException {
        InputStream inputStream = fileURL.openConnection().getInputStream();
        JSONObject jsonObject = JSONObject.parseObject(
            new String(inputStream.readAllBytes(), StandardCharsets.UTF_8),
            JSONObject.class,
            Feature.OrderedField
        );
        inputStream.close();
        return jsonObject;
    }

    public static void modifyByFileURI(JSONObject jsonObject, URL fileURL, URL rootSchemaFileURL) {
        jsonObject.remove(KEY_WORD_SCHEMA);
        if (rootSchemaFileURL != null) {
            jsonObject.put(KEY_WORD_SCHEMA, rootSchemaFileURL.toString());
        }
        jsonObject.remove(KEY_WORD_ID);
        jsonObject.put(KEY_WORD_ID, fileURL.toString());
    }

    public URL getFileURL() {
        return fileURL;
    }

    public JSONObject getJsonObject() {
        return (JSONObject) jsonObject.clone();
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
