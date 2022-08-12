package cc.xfl12345.mybigdata.server.model.database.table.curd.orm.config;

public interface BeeOrmTableHandlerConfig<TablePojoType> {
    String getTableName();

    String getIdFieldName();

    Long getId(TablePojoType value);

    TablePojoType getNewPojoInstance();
}
