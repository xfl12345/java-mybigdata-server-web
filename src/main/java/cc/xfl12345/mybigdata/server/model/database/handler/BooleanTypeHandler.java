package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.BooleanTypeResult;

public interface BooleanTypeHandler {
    BooleanTypeResult insertBoolean(Boolean theBoolean);

    BooleanTypeResult selectBooleanByGlobalId(Long globalId);

    BooleanTypeResult updateBooleanByGlobalId(Boolean theBoolean, Long globalId);
}
