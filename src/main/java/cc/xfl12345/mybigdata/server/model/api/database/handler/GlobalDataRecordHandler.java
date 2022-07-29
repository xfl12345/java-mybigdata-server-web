package cc.xfl12345.mybigdata.server.model.api.database.handler;

import cc.xfl12345.mybigdata.server.model.api.database.result.SingleDataResultBase;
import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import org.teasoft.bee.osql.Condition;

public interface GlobalDataRecordHandler {
    SingleDataResultBase insert(GlobalDataRecord value);

    SingleDataResultBase select(Condition condition);

    SingleDataResultBase update(Condition condition);

    SingleDataResultBase delete(Condition condition);
}
