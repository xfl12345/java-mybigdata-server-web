package cc.xfl12345.mybigdata.server.model.database.table.curd.orm.config;

import java.util.concurrent.ConcurrentHashMap;

public class OrmTableHandlerConfigMapperBase<ConfigType> implements OrmTableHandlerConfigMapper<ConfigType> {
    protected ConcurrentHashMap<Class<?>, ConfigType> map;

    public OrmTableHandlerConfigMapperBase() {
        map = new ConcurrentHashMap<>();
    }

    public OrmTableHandlerConfigMapperBase(int initialCapacity) {
        map = new ConcurrentHashMap<>(initialCapacity);
    }


    public <TablePojoType> void putConfig(Class<TablePojoType> cls, ConfigType config) {
        map.put(cls, config);
    }

    public <TablePojoType> ConfigType getConfig(Class<TablePojoType> cls) {
        return tryCast(map.get(cls), cls);
    }

    public <TablePojoType> ConfigType removeConfig(Class<TablePojoType> cls) {
        return tryCast(map.remove(cls), cls);
    }

    @SuppressWarnings("unchecked")
    private <TablePojoType> ConfigType tryCast(ConfigType config, Class<TablePojoType> cls) {
        return config == null ? null : (ConfigType) config;
    }
}

