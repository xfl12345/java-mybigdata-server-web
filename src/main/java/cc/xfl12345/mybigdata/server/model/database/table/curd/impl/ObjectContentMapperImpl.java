package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.ObjectContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.ObjectContent;

public class ObjectContentMapperImpl
    extends AbstractAppTableMapper<ObjectContent>
    implements ObjectContentMapper {
    @Override
    public Class<ObjectContent> getTablePojoType() {
        return ObjectContent.class;
    }
}




