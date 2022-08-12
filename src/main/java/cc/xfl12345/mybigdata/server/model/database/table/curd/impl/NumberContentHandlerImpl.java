package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.NumberContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.NumberContent;

public class NumberContentHandlerImpl
    extends AbstractAppTableHandler<NumberContent>
    implements NumberContentHandler {
    @Override
    public Class<NumberContent> getTablePojoType() {
        return NumberContent.class;
    }
}




