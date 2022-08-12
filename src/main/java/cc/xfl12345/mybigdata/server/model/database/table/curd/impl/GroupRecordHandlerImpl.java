package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.GroupRecordHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GroupRecord;

public class GroupRecordHandlerImpl
    extends AbstractAppTableHandler<GroupRecord>
    implements GroupRecordHandler {
    @Override
    public Class<GroupRecord> getTablePojoType() {
        return GroupRecord.class;
    }
}




