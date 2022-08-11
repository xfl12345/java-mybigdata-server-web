package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.table.constant.GlobalDataRecordConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.GlobalDataRecordHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.BeeOrmCoreTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;

public class GlobalDataRecordHandlerImpl extends BeeOrmCoreTableHandler<GlobalDataRecord> implements GlobalDataRecordHandler {
    @Override
    protected String getTableName() {
        return CoreTableNames.GLOBAL_DATA_RECORD;
    }

    @Override
    protected String getIdFieldName() {
        return GlobalDataRecordConstant.ID;
    }

    @Override
    protected Long getId(GlobalDataRecord value) {
        return value.getId();
    }

    @Override
    protected GlobalDataRecord getNewPojoInstance() {
        return new GlobalDataRecord();
    }
}




