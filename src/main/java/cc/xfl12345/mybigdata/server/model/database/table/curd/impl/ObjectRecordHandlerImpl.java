package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.ObjectRecordHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.ObjectRecord;

public class ObjectRecordHandlerImpl
    extends AbstractAppTableHandler<ObjectRecord>
    implements ObjectRecordHandler {
    @Override
    public Class<ObjectRecord> getTablePojoType() {
        return ObjectRecord.class;
    }
}




