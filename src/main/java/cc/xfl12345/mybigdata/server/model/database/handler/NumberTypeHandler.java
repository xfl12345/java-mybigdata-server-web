package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.NumberTypeResult;

import java.math.BigDecimal;

public interface NumberTypeHandler {
    NumberTypeResult insertNumber(BigDecimal number);

    NumberTypeResult selectNumber(BigDecimal number);

    NumberTypeResult updateNumberByGlobalId(BigDecimal number, Long globalId);

    NumberTypeResult deleteNumber(BigDecimal number);
}
