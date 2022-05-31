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
    }

    @Override
    public void destroy() throws Exception {
    }
}
