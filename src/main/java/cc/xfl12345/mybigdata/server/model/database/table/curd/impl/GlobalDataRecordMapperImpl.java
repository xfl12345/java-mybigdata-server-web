package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.GlobalDataRecordMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;

public class GlobalDataRecordMapperImpl
    extends AbstractAppTableMapper<GlobalDataRecord>
    implements GlobalDataRecordMapper {
    @Override
    public Class<GlobalDataRecord> getTablePojoType() {
        return GlobalDataRecord.class;
    }
}




