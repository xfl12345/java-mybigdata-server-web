package cc.xfl12345.mybigdata.server.model.api.database.handler.impl;

import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.CoreTableCache;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractTableHandler implements DisposableBean, InitializingBean {
    protected String fieldCanNotBeNullMessageTemplate = "Property [%s] can not be null!";

    @Getter
    @Setter
    protected volatile CoreTableCache coreTableCache = null;

    @Getter
    @Setter
    protected volatile SqlErrorHandler sqlErrorHandler = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (coreTableCache == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("coreTableCache"));
        }
        if (sqlErrorHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("sqlErrorHandler"));
        }
    }

    @Override
    public void destroy() throws Exception {
    }
}
