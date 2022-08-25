package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.data.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.service.StringTypeService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({DataServiceConfig.class})
public class DataHandlerConfig {
    @Bean
    public StringTypeHandler stringTypeHandler(StringTypeService dataService) {
        StringTypeHandler handler = new StringTypeHandler();
        handler.setDataService(dataService);
        return handler;
    }
}
