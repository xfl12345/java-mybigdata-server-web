package cc.xfl12345.mybigdata.server.model.api.database.handler;

import cc.xfl12345.mybigdata.server.model.api.database.result.DictionaryTypeResult;
import cc.xfl12345.mybigdata.server.model.api.database.result.SingleDataResultBase;

public interface ObjectTypeHandler {
    DictionaryTypeResult insert(Object obj, Long jsonSchemaId);

    SingleDataResultBase select(Long globalId);

    SingleDataResultBase update(Object obj, Long globalId);

    SingleDataResultBase delete(Long globalId);
}
