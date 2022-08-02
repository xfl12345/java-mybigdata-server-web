package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.ObjectContent;
import com.alibaba.fastjson2.JSONObject;

import java.util.Collection;
import java.util.Map;

public interface ObjectTypeHandler {
    /**
     * 插入对象，返回 全局数据记录表 的 ID
     * @return 全局数据记录表 的 ID
     */
    Long insert(String name, Object obj, Long jsonSchemaId) throws Exception;

    JSONObject select(Long globalId);

    void update(Map<String, Long> keyValuePairs, Long globalId) throws TableOperationException;

    void update(Collection<ObjectContent> keyValuePairs, Long globalId) throws TableOperationException;

    void deleteById(Long globalId) throws Exception;
}
