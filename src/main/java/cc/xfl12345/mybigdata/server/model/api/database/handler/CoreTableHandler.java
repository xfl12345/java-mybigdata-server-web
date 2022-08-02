package cc.xfl12345.mybigdata.server.model.api.database.handler;

import cc.xfl12345.mybigdata.server.model.api.database.result.ExecuteResultBase;
import cc.xfl12345.mybigdata.server.model.api.database.result.SingleDataResultBase;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface CoreTableHandler extends DisposableBean, InitializingBean {

    SingleDataResultBase selectById(Long globalId);

    SingleDataResultBase deleteById(Long globalId);

    SingleDataResultBase updateById(Long globalId, Object value);


    SingleDataResultBase addNumber(BigDecimal value);

    BigDecimal getNumberById(Long globalId);

    ExecuteResultBase updateNumberById(Long globalId, BigDecimal value);


    SingleDataResultBase addString(String value);

    String getStringById(Long globalId);

    ExecuteResultBase updateStringById(Long globalId, String value);


    SingleDataResultBase addArray(List<Long> value);

    ExecuteResultBase addArrayItemById(Long globalId, List<Long> items);

    List<Long> getArrayById(Long globalId);

    ExecuteResultBase updateArrayById(Long globalId, List<Long> items);


    SingleDataResultBase addSet(Set<Long> value);

    ExecuteResultBase addSetItemById(Long globalId, Set<Long> items);

    Set<Long> getSetById(Long globalId);

    ExecuteResultBase updateSetById(Long globalId, Set<Long> items);


    SingleDataResultBase addObject(Long schemaId, JSONObject value);

    ExecuteResultBase updateObjectById(Long globalId, Long schemaId, JSONObject value);

    ExecuteResultBase updateObjectFieldById(Long globalId, String key, Long valueGlobalId);

    JSONObject getObjectById(Long globalId);
}
