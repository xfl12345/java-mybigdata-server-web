package cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.base.TablePojoTypeGetter;

public abstract class AbstractTypedTableHandler<TablePojoType>
    extends AbstractTableHandler implements TablePojoTypeGetter<TablePojoType> {

    public abstract Class<TablePojoType> getTablePojoType();
}