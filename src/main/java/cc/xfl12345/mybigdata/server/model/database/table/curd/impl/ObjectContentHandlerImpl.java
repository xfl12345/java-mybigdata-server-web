package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.table.constant.ObjectContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.ObjectContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.BeeOrmCoreTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.ObjectContent;

public class ObjectContentHandlerImpl extends BeeOrmCoreTableHandler<ObjectContent> implements ObjectContentHandler {
    @Override
    protected String getTableName() {
        return CoreTableNames.OBJECT_CONTENT;
    }

    @Override
    protected String getIdFieldName() {
        return ObjectContentConstant.GLOBAL_ID;
    }

    @Override
    protected Long getId(ObjectContent value) {
        return value.getGlobalId();
    }

    @Override
    protected ObjectContent getNewPojoInstance() {
        return new ObjectContent();
    }
}




