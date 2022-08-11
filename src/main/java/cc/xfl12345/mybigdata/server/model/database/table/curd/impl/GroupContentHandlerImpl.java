package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.table.constant.GroupContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.GroupContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.BeeOrmCoreTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GroupContent;

public class GroupContentHandlerImpl extends BeeOrmCoreTableHandler<GroupContent> implements GroupContentHandler {
    @Override
    protected String getTableName() {
        return CoreTableNames.GROUP_CONTENT;
    }

    @Override
    protected String getIdFieldName() {
        return GroupContentConstant.GLOBAL_ID;
    }

    @Override
    protected Long getId(GroupContent value) {
        return value.getGlobalId();
    }

    @Override
    protected GroupContent getNewPojoInstance() {
        return new GroupContent();
    }
}




