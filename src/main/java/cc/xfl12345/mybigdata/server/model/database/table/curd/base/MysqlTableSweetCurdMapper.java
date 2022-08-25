package cc.xfl12345.mybigdata.server.model.database.table.curd.base;

public interface MysqlTableSweetCurdMapper<ValueType, ConditionType>
    extends TableCurdMapper<Long, ValueType, ConditionType>, ConditionSweet<Long, ConditionType> {
}
