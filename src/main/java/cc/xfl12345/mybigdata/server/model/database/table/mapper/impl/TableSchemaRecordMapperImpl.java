package cc.xfl12345.mybigdata.server.model.database.table.mapper.impl;

import cc.xfl12345.mybigdata.server.model.database.table.mapper.TableSchemaRecordMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.TableSchemaRecord;

public class TableSchemaRecordMapperImpl
    extends AbstractAppTableMapper<TableSchemaRecord>
    implements TableSchemaRecordMapper {
    @Override
    public Class<TableSchemaRecord> getTablePojoType() {
        return TableSchemaRecord.class;
    }
}




