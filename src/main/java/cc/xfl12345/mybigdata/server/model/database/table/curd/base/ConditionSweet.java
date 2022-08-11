package cc.xfl12345.mybigdata.server.model.database.table.curd.base;

public interface ConditionSweet<IdType, ConditionType> {
    ConditionType getConditionWithSelectedFields(String... fields);

    void addFields2Condition(ConditionType condition, String... fields);

    ConditionType getConditionWithId(IdType id);

    void addId2Condition(ConditionType condition, IdType id);
}
