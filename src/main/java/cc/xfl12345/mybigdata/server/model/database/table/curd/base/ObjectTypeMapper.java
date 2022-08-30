package cc.xfl12345.mybigdata.server.model.database.table.curd.base;

import com.networknt.schema.JsonSchema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public abstract class ObjectTypeMapper implements Map<String, Object> {
    @Getter
    @Setter
    protected Object id;

    @Getter
    @Setter
    protected JsonSchema jsonSchema;
}
