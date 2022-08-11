package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.table.constant.${tableClass.shortClassName}Constant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.${tableClass.shortClassName}Handler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.BeeOrmCoreTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.${tableClass.shortClassName};

public class ${tableClass.shortClassName}HandlerImpl extends BeeOrmCoreTableHandler<${tableClass.shortClassName}> implements ${tableClass.shortClassName}Handler {
    @Override
    protected String getTableName() {
        return null;
    }

    @Override
    protected String getIdFieldName() {
        return null;
    }

    @Override
    protected Long getId(${tableClass.shortClassName} value) {
        return null;
    }

    @Override
    protected ${tableClass.shortClassName} getNewPojoInstance() {
        return new ${tableClass.shortClassName}();
    }
}




