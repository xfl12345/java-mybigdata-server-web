package cc.xfl12345.mybigdata.server.model.database.table.mapper.impl;

import cc.xfl12345.mybigdata.server.model.database.table.mapper.NumberContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.NumberContent;

public class NumberContentMapperImpl
    extends AbstractAppTableMapper<NumberContent>
    implements NumberContentMapper {
    @Override
    public Class<NumberContent> getTablePojoType() {
        return NumberContent.class;
    }
}




