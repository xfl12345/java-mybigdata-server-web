package cc.xfl12345.mybigdata.server.model.database.table.mapper.impl;

import cc.xfl12345.mybigdata.server.model.database.table.mapper.StringContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;

public class StringContentMapperImpl
    extends AbstractAppTableMapper<StringContent>
    implements StringContentMapper {
    @Override
    public Class<StringContent> getTablePojoType() {
        return StringContent.class;
    }
}




