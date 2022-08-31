package cc.xfl12345.mybigdata.server.model.database.table.mapper.orm.bee.config;

public interface BeeTableMapperConfig<TablePojoType> {
    String getTableName();

    String getIdFieldName();

    Long getId(TablePojoType value);

    TablePojoType getNewPojoInstance();
}
