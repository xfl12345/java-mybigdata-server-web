package cc.xfl12345.mybigdata.server.model.database.table.curd.base;

public interface MysqlTableSweetCurdHandler<ValueType, ConditionType>
    extends TableCurdHandler<Long, ValueType, ConditionType>, ConditionSweet<Long, ConditionType> {
}
