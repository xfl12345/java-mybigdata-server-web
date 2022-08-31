package cc.xfl12345.mybigdata.server.model.database.table.mapper.impl;

import cc.xfl12345.mybigdata.server.model.database.table.mapper.ObjectRecordMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.ObjectRecord;

public class ObjectRecordMapperImpl
    extends AbstractAppTableMapper<ObjectRecord>
    implements ObjectRecordMapper {
    @Override
    public Class<ObjectRecord> getTablePojoType() {
        return ObjectRecord.class;
    }
}




