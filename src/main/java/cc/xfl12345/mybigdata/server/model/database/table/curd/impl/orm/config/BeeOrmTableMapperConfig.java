package cc.xfl12345.mybigdata.server.model.database.table.curd.impl.orm.config;

public interface BeeOrmTableMapperConfig<TablePojoType> {
    String getTableName();

    String getIdFieldName();

    Long getId(TablePojoType value);

    TablePojoType getNewPojoInstance();
}
