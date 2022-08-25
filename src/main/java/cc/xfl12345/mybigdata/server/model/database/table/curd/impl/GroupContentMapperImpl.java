package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.GroupContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GroupContent;

public class GroupContentMapperImpl
    extends AbstractAppTableMapper<GroupContent>
    implements GroupContentMapper {
    @Override
    public Class<GroupContent> getTablePojoType() {
        return GroupContent.class;
    }
}




