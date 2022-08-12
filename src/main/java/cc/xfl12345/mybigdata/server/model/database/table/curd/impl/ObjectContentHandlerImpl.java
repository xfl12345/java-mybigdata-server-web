package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.ObjectContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.ObjectContent;

public class ObjectContentHandlerImpl
    extends AbstractAppTableHandler<ObjectContent>
    implements ObjectContentHandler {
    @Override
    public Class<ObjectContent> getTablePojoType() {
        return ObjectContent.class;
    }
}




