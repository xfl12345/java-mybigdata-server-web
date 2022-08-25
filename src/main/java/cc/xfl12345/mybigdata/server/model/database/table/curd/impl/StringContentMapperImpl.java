package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.StringContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;

public class StringContentMapperImpl
    extends AbstractAppTableMapper<StringContent>
    implements StringContentMapper {
    @Override
    public Class<StringContent> getTablePojoType() {
        return StringContent.class;
    }
}




