package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.TableSchemaRecordHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.TableSchemaRecord;

public class TableSchemaRecordHandlerImpl
    extends AbstractAppTableHandler<TableSchemaRecord>
    implements TableSchemaRecordHandler {
    @Override
    public Class<TableSchemaRecord> getTablePojoType() {
        return TableSchemaRecord.class;
    }
}




