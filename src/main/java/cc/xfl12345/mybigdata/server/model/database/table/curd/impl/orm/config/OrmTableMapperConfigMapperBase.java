package cc.xfl12345.mybigdata.server.model.database.table.curd.impl.orm.config;

import java.util.concurrent.ConcurrentHashMap;

public class OrmTableMapperConfigMapperBase<ConfigType> implements OrmTableMapperConfigMapper<ConfigType> {
    protected ConcurrentHashMap<Class<?>, ConfigType> map;

    public OrmTableMapperConfigMapperBase() {
        map = new ConcurrentHashMap<>();
    }

    public OrmTableMapperConfigMapperBase(int initialCapacity) {
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

