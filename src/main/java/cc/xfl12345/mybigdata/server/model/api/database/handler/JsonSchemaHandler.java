package cc.xfl12345.mybigdata.server.model.api.database.handler;

import cc.xfl12345.mybigdata.server.model.api.database.result.JsonTypeResult;
import org.teasoft.bee.osql.Condition;

public interface JsonSchemaHandler {
    JsonTypeResult insertJsonSchema(String userDefineName, String jsonSchema);

    JsonTypeResult selectJsonSchemaByName(String userDefineName);

    JsonTypeResult updateJsonSchema(Condition condition, String jsonSchema);

    JsonTypeResult updateJsonSchemaByName(String userDefineName, String jsonSchema);

    JsonTypeResult updateJsonSchemaByGlobalId(Long globalId, String jsonSchema);

    JsonTypeResult updateJsonSchemaNameByGlobalId(Long globalId, String userDefineName);

    JsonTypeResult deleteJsonSchemaByName(String userDefineName);
}
