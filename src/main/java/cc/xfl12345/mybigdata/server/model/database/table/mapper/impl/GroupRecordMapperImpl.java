package cc.xfl12345.mybigdata.server.model.database.table.mapper.impl;

import cc.xfl12345.mybigdata.server.model.database.table.mapper.GroupRecordMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GroupRecord;

public class GroupRecordMapperImpl
    extends AbstractAppTableMapper<GroupRecord>
    implements GroupRecordMapper {
    @Override
    public Class<GroupRecord> getTablePojoType() {
        return GroupRecord.class;
    }
}




