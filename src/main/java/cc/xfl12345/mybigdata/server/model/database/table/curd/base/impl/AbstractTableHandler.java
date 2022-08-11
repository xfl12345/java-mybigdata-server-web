package cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractTableHandler implements DisposableBean, InitializingBean {
    protected String fieldCanNotBeNullMessageTemplate = "Property [%s] can not be null!";

    @Getter
    protected volatile CoreTableCache coreTableCache = null;

    @Autowired
    public void setCoreTableCache(CoreTableCache coreTableCache) {
        this.coreTableCache = coreTableCache;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (coreTableCache == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("coreTableCache"));
        }
    }

    @Override
    public void destroy() throws Exception {
    }
}
