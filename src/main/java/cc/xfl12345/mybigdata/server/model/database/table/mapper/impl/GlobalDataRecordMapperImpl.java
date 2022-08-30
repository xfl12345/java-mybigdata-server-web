package cc.xfl12345.mybigdata.server.model.database.table.mapper.impl;

import cc.xfl12345.mybigdata.server.model.database.table.mapper.GlobalDataRecordMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;

public class GlobalDataRecordMapperImpl
    extends AbstractAppTableMapper<GlobalDataRecord>
    implements GlobalDataRecordMapper {
    @Override
    public Class<GlobalDataRecord> getTablePojoType() {
        return GlobalDataRecord.class;
    }
}




