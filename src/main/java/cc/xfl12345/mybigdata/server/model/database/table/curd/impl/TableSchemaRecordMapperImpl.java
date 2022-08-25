package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.TableSchemaRecordMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.TableSchemaRecord;

public class TableSchemaRecordMapperImpl
    extends AbstractAppTableMapper<TableSchemaRecord>
    implements TableSchemaRecordMapper {
    @Override
    public Class<TableSchemaRecord> getTablePojoType() {
        return TableSchemaRecord.class;
    }
}




