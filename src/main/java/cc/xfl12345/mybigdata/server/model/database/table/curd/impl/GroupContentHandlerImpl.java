package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.GroupContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GroupContent;

public class GroupContentHandlerImpl
    extends AbstractAppTableHandler<GroupContent>
    implements GroupContentHandler {
    @Override
    public Class<GroupContent> getTablePojoType() {
        return GroupContent.class;
    }
}




