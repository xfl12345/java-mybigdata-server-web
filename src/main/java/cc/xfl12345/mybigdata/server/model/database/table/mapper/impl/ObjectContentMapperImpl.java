package cc.xfl12345.mybigdata.server.model.database.table.mapper.impl;

import cc.xfl12345.mybigdata.server.model.database.table.mapper.ObjectContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.ObjectContent;

public class ObjectContentMapperImpl
    extends AbstractAppTableMapper<ObjectContent>
    implements ObjectContentMapper {
    @Override
    public Class<ObjectContent> getTablePojoType() {
        return ObjectContent.class;
    }
}




