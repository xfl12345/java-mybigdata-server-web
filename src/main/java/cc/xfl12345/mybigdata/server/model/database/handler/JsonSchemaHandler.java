package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.TableSchemaRecord;
import com.alibaba.fastjson2.JSONObject;

import java.util.List;

public interface JsonSchemaHandler {
    Long insert(JSONObject jsonSchema) throws Exception;

    List<TableSchemaRecord> selectJsonSchemaByKeyWords(String... keys);

    void update(TableSchemaRecord tableSchemaRecord) throws TableOperationException;

    void deleteById(Long globalId) throws TableOperationException;
}
