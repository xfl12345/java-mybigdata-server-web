package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.JsonTypeResult;
import com.alibaba.fastjson2.JSONObject;

public interface JsonSchemaHandler {
    JsonTypeResult insertJsonSchema(JSONObject jsonObject, String userDefineName);

    JsonTypeResult selectJsonSchemaByName(String userDefineName);

    JsonTypeResult updateJsonSchemaByName(JSONObject jsonObject, String userDefineName);

    JsonTypeResult updateJsonSchemaByGlobalId(JSONObject jsonObject, Long globalId);

    JsonTypeResult updateJsonSchemaNameByGlobalId(String userDefineName, Long globalId);

    JsonTypeResult deleteJsonSchemaByName(String userDefineName);
}
