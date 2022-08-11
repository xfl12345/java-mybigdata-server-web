package cc.xfl12345.mybigdata.server.model.database.table.curd.base;

import org.teasoft.bee.osql.Condition;

public interface BeeOrmCurdHandler<ValueType> extends MysqlTableSweetCurdHandler<ValueType, Condition> {
}
