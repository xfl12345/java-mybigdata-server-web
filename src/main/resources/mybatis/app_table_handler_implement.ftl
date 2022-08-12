package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.${tableClass.shortClassName}Handler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.${tableClass.shortClassName};

public class ${tableClass.shortClassName}HandlerImpl
    extends AbstractAppTableHandler<${tableClass.shortClassName}>
    implements ${tableClass.shortClassName}Handler {
    @Override
    public Class<${tableClass.shortClassName}> getTablePojoType() {
        return ${tableClass.shortClassName}.class;
    }
}




