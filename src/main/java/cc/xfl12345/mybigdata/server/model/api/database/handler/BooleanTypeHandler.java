package cc.xfl12345.mybigdata.server.model.api.database.handler;

import cc.xfl12345.mybigdata.server.model.api.database.result.BooleanTypeResult;

public interface BooleanTypeHandler {
    BooleanTypeResult insertBoolean(Boolean theBoolean);

    BooleanTypeResult selectBooleanByGlobalId(Long globalId);

    BooleanTypeResult updateBooleanByGlobalId(Long globalId, Boolean theBoolean);
}
