package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.NumberContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.NumberContent;

public class NumberContentMapperImpl
    extends AbstractAppTableMapper<NumberContent>
    implements NumberContentMapper {
    @Override
    public Class<NumberContent> getTablePojoType() {
        return NumberContent.class;
    }
}




