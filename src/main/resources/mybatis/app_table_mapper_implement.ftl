package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.${tableClass.shortClassName}Mapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.${tableClass.shortClassName};

public class ${tableClass.shortClassName}MapperImpl
    extends AbstractAppTableMapper<${tableClass.shortClassName}>
    implements ${tableClass.shortClassName}Mapper {
    @Override
    public Class<${tableClass.shortClassName}> getTablePojoType() {
        return ${tableClass.shortClassName}.class;
    }
}




