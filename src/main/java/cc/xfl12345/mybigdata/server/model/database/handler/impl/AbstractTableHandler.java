package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.model.database.handler.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.producer.impl.GlobalDataRecordProducer;
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
    protected volatile GlobalDataRecordProducer globalDataRecordProducer = null;

    @Getter
    @Setter
    protected volatile SqlErrorHandler sqlErrorHandler = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (globalDataRecordProducer == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("globalDataRecordProducer"));
        }
        if (coreTableCache == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("coreTableCache"));
        }
        if (sqlErrorHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("sqlErrorHandler"));
        }
    }

    @Override
    public void destroy() throws Exception {
        globalDataRecordProducer.destroy();
    }
}
