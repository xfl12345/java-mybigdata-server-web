package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.table.constant.ObjectRecordConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.ObjectRecordHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.BeeOrmCoreTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.ObjectRecord;


public class ObjectRecordHandlerImpl extends BeeOrmCoreTableHandler<ObjectRecord> implements ObjectRecordHandler {
    @Override
    protected String getTableName() {
        return CoreTableNames.OBJECT_RECORD;
    }

    @Override
    protected String getIdFieldName() {
        return ObjectRecordConstant.GLOBAL_ID;
    }

    @Override
    protected Long getId(ObjectRecord value) {
        return value.getGlobalId();
    }

    @Override
    protected ObjectRecord getNewPojoInstance() {
        return new ObjectRecord();
    }
}




