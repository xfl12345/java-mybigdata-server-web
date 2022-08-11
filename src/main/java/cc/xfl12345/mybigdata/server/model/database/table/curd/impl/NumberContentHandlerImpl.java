package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.table.constant.NumberContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.NumberContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.BeeOrmCoreTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.NumberContent;

public class NumberContentHandlerImpl extends BeeOrmCoreTableHandler<NumberContent> implements NumberContentHandler {
    @Override
    protected String getTableName() {
        return CoreTableNames.NUMBER_CONTENT;
    }

    @Override
    protected String getIdFieldName() {
        return NumberContentConstant.GLOBAL_ID;
    }

    @Override
    protected Long getId(NumberContent value) {
        return value.getGlobalId();
    }

    @Override
    protected NumberContent getNewPojoInstance() {
        return new NumberContent();
    }
}




