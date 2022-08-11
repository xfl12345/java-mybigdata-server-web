package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.table.constant.GroupRecordConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.GroupRecordHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.BeeOrmCoreTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GroupRecord;

public class GroupRecordHandlerImpl extends BeeOrmCoreTableHandler<GroupRecord> implements GroupRecordHandler {
    @Override
    protected String getTableName() {
        return CoreTableNames.GROUP_RECORD;
    }

    @Override
    protected String getIdFieldName() {
        return GroupRecordConstant.GLOBAL_ID;
    }

    @Override
    protected Long getId(GroupRecord value) {
        return value.getGlobalId();
    }

    @Override
    protected GroupRecord getNewPojoInstance() {
        return new GroupRecord();
    }
}




