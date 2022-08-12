package cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTables;
import cc.xfl12345.mybigdata.server.pojo.TwoWayMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public abstract class AbstractCoreTableCache <IdType, ValueType> implements InitializingBean {
    @Getter
    protected TwoWayMap<ValueType, IdType> tableNameCache;

    @Getter
    protected IdType idOfTrue;

    @Getter
    protected IdType idOfFalse;

    public AbstractCoreTableCache() {
        tableNameCache = new TwoWayMap<>(CoreTables.values().length);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        refreshBooleanCache();
        refreshCoreTableNameCache();
    }

    public abstract void refreshBooleanCache() throws Exception;

    public abstract void refreshCoreTableNameCache() throws Exception;
}
