package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.DictionaryTypeResult;
import cc.xfl12345.mybigdata.server.model.database.result.SingleDataResultBase;

public interface ObjectTypeHandler {
    DictionaryTypeResult insert(Object obj, Long jsonSchemaId);

    SingleDataResultBase select(Long globalId);

    SingleDataResultBase update(Object obj, Long globalId);

    SingleDataResultBase delete(Long globalId);
}
