package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.table.constant.TableSchemaRecordConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.TableSchemaRecordHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.BeeOrmCoreTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.TableSchemaRecord;

public class TableSchemaRecordHandlerImpl extends BeeOrmCoreTableHandler<TableSchemaRecord> implements TableSchemaRecordHandler {
    @Override
    protected String getTableName() {
        return CoreTableNames.TABLE_SCHEMA_RECORD;
    }

    @Override
    protected String getIdFieldName() {
        return TableSchemaRecordConstant.GLOBAL_ID;
    }

    @Override
    protected Long getId(TableSchemaRecord value) {
        return value.getGlobalId();
    }

    @Override
    protected TableSchemaRecord getNewPojoInstance() {
        return new TableSchemaRecord();
    }
}




