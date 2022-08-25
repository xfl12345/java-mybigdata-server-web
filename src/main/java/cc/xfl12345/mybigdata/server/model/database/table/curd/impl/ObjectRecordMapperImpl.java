package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.ObjectRecordMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.ObjectRecord;

public class ObjectRecordMapperImpl
    extends AbstractAppTableMapper<ObjectRecord>
    implements ObjectRecordMapper {
    @Override
    public Class<ObjectRecord> getTablePojoType() {
        return ObjectRecord.class;
    }
}




