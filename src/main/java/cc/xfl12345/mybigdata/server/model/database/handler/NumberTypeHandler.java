package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.NumberTypeResult;

import java.math.BigDecimal;

public interface NumberTypeHandler {
    NumberTypeResult insertNumber(BigDecimal number);

    NumberTypeResult selectNumber(BigDecimal number);

    // 更新数字 有可能是伪需求。因为数字是常量，如果需要更新，那也更多的是更新引用，而非修改内容。
    NumberTypeResult updateNumberByGlobalId(BigDecimal number, Long globalId);

    NumberTypeResult deleteNumber(BigDecimal number);
}
