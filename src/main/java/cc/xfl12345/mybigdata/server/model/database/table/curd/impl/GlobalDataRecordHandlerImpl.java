package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.GlobalDataRecordHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;

public class GlobalDataRecordHandlerImpl
    extends AbstractAppTableHandler<GlobalDataRecord>
    implements GlobalDataRecordHandler {
    @Override
    public Class<GlobalDataRecord> getTablePojoType() {
        return GlobalDataRecord.class;
    }
}




