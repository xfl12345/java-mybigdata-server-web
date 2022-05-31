package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.model.database.producer.GlobalDataRecordProducer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractTableHandler implements DisposableBean, InitializingBean {
    @Getter
    @Setter
    protected volatile CoreTableCache coreTableCache = null;

    @Getter
    @Setter
    protected volatile GlobalDataRecordProducer globalDataRecordProducer = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (globalDataRecordProducer == null) {
            throw new IllegalArgumentException("Property [globalDataRecordProducer] can not be null!");
        }
        if (coreTableCache == null) {
            throw new IllegalArgumentException("Property [coreTableCache] can not be null!");
        }
    }

    @Override
    public void destroy() throws Exception {
        globalDataRecordProducer.destroy();
    }
}
