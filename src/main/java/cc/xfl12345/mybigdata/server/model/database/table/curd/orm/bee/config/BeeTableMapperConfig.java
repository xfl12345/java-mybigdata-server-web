package cc.xfl12345.mybigdata.server.model.database.table.curd.orm.bee.config;

public interface BeeTableMapperConfig<TablePojoType> {
    String getTableName();

    String getIdFieldName();

    Long getId(TablePojoType value);

    TablePojoType getNewPojoInstance();
}
