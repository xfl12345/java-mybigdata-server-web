package cc.xfl12345.mybigdata.server.model.api.database.handler.impl;

import cc.xfl12345.mybigdata.server.model.database.handler.*;
import cc.xfl12345.mybigdata.server.model.api.database.handler.CoreTableHandler;
import cc.xfl12345.mybigdata.server.model.api.database.result.ExecuteResultBase;
import cc.xfl12345.mybigdata.server.model.api.database.result.SingleDataResultBase;
import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.CoreTableCache;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Slf4j
public class CoreTableHandlerImpl implements CoreTableHandler, DisposableBean, InitializingBean {
    protected String fieldCanNotBeNullMessageTemplate = "Property [%s] can not be null!";


    @Getter
    @Setter
    protected volatile StringTypeHandler stringTypeHandler;

    @Getter
    @Setter
    protected volatile NumberTypeHandler numberTypeHandler;

    @Getter
    @Setter
    protected volatile GroupTypeHandler groupTypeHandler;

    @Getter
    @Setter
    protected volatile ObjectTypeHandler objectTypeHandler;

    @Getter
    @Setter
    protected volatile GlobalDataRecordHandler globalDataRecordHandler;

    @Getter
    @Setter
    protected volatile JsonSchemaHandler jsonSchemaHandler;

    @Getter
    @Setter
    protected volatile AccountHandler accountHandler;


    @Getter
    @Setter
    protected volatile CoreTableCache coreTableCache = null;

    @Getter
    @Setter
    protected volatile SqlErrorHandler sqlErrorHandler = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (coreTableCache == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("coreTableCache"));
        }
        if (sqlErrorHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("sqlErrorHandler"));
        }


        if (stringTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("stringTypeHandler"));
        }
        if (numberTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("numberTypeHandler"));
        }
        if (groupTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("groupTypeHandler"));
        }
        if (objectTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("objectTypeHandler"));
        }
        if (globalDataRecordHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("globalDataRecordHandler"));
        }
        if (jsonSchemaHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("jsonSchemaHandler"));
        }
        if (accountHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("accountHandler"));
        }
    }

    @Override
    public void destroy() throws Exception {
    }


    @Override
    public SingleDataResultBase selectById(Long globalId) {
        return null;
    }

    @Override
    public SingleDataResultBase deleteById(Long globalId) {
        return null;
    }

    @Override
    public SingleDataResultBase updateById(Long globalId, Object value) {
        return null;
    }


    @Override
    public SingleDataResultBase addNumber(BigDecimal value) {
        return null;
    }

    @Override
    public BigDecimal getNumberById(Long globalId) {
        return null;
    }

    @Override
    public ExecuteResultBase updateNumberById(Long globalId, BigDecimal value) {
        return null;
    }


    @Override
    public SingleDataResultBase addString(String value) {
        return null;
    }

    @Override
    public String getStringById(Long globalId) {
        return null;
    }

    @Override
    public ExecuteResultBase updateStringById(Long globalId, String value) {
        return null;
    }

    @Override
    public SingleDataResultBase addArray(List<Long> value) {
        return null;
    }

    @Override
    public ExecuteResultBase addArrayItemById(Long globalId, List<Long> items) {
        return null;
    }

    @Override
    public List<Long> getArrayById(Long globalId) {
        return null;
    }

    @Override
    public ExecuteResultBase updateArrayById(Long globalId, List<Long> items) {
        return null;
    }


    @Override
    public SingleDataResultBase addSet(Set<Long> value) {
        return null;
    }

    @Override
    public ExecuteResultBase addSetItemById(Long globalId, Set<Long> items) {
        return null;
    }

    @Override
    public Set<Long> getSetById(Long globalId) {
        return null;
    }

    @Override
    public ExecuteResultBase updateSetById(Long globalId, Set<Long> items) {
        return null;
    }


    @Override
    public SingleDataResultBase addObject(Long schemaId, JSONObject value) {
        return null;
    }

    @Override
    public ExecuteResultBase updateObjectById(Long globalId, Long schemaId, JSONObject value) {
        return null;
    }

    @Override
    public ExecuteResultBase updateObjectFieldById(Long globalId, String key, Long valueGlobalId) {
        return null;
    }

    @Override
    public JSONObject getObjectById(Long globalId) {
        return null;
    }


}
