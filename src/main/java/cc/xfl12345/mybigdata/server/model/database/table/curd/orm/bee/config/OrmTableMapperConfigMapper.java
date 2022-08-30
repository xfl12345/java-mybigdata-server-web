package cc.xfl12345.mybigdata.server.model.database.table.curd.orm.bee.config;

public interface OrmTableMapperConfigMapper<ConfigType> {
    <TablePojoType> void putConfig(Class<TablePojoType> cls, ConfigType config);

    <TablePojoType> ConfigType getConfig(Class<TablePojoType> cls);

    <TablePojoType> ConfigType removeConfig(Class<TablePojoType> cls);
}
