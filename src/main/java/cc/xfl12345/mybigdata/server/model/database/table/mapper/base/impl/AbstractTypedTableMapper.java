package cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl;

import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.TablePojoTypeGetter;

public abstract class AbstractTypedTableMapper<TablePojoType>
    extends AbstractTableMapper implements TablePojoTypeGetter<TablePojoType> {

    public abstract Class<TablePojoType> getTablePojoType();
}
