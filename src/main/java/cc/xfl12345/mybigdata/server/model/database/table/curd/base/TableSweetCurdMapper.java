package cc.xfl12345.mybigdata.server.model.database.table.curd.base;

public interface TableSweetCurdMapper<ValueType, ConditionType>
    extends TableCurdMapper<ValueType, ConditionType>, ConditionSweet<ConditionType> {
}
