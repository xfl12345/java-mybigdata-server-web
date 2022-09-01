package cc.xfl12345.mybigdata.server.model.database.table.mapper.impl;

import cc.xfl12345.mybigdata.server.model.database.table.mapper.GroupContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GroupContent;

public class GroupContentMapperImpl
    extends AbstractAppTableMapper<GroupContent>
    implements GroupContentMapper {
    @Override
    public Class<GroupContent> getTablePojoType() {
        return GroupContent.class;
    }
}



