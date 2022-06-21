package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.JsonTypeResult;
import net.jimblackler.jsonschemafriend.Schema;
import org.teasoft.bee.osql.Condition;

public interface JsonSchemaHandler {
    JsonTypeResult insertJsonSchema(String userDefineName, Schema jsonSchema);

    JsonTypeResult selectJsonSchemaByName(String userDefineName);

    JsonTypeResult updateJsonSchema(Condition condition, Schema jsonSchema);

    JsonTypeResult updateJsonSchemaByName(String userDefineName, Schema jsonSchema);

    JsonTypeResult updateJsonSchemaByGlobalId(Long globalId, Schema jsonSchema);

    JsonTypeResult updateJsonSchemaNameByGlobalId(Long globalId, String userDefineName);

    JsonTypeResult deleteJsonSchemaByName(String userDefineName);
}
