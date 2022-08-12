package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.StringTypeHandlerImpl;
import org.springframework.context.annotation.Bean;

public class BasicDataHandlerConfig {
    @Bean("stringTypeHandler")
    public StringTypeHandler getStringTypeHandler() throws Exception {
        return new StringTypeHandlerImpl();
    }
}
