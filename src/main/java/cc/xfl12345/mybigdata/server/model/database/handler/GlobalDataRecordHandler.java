package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.SingleDataResultBase;
import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;

public interface GlobalDataRecordHandler {
    SingleDataResultBase insert(GlobalDataRecord value);

    SingleDataResultBase select(GlobalDataRecord value);

    SingleDataResultBase update(GlobalDataRecord value);

    SingleDataResultBase delete(GlobalDataRecord value);
}
