package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.service.StringTypeService;
import cc.xfl12345.mybigdata.server.model.database.service.impl.StringTypeServiceImpl;
import cc.xfl12345.mybigdata.server.model.database.table.curd.StringContentMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({MapperConfig.class})
public class DataServiceConfig {
    @Bean
    public StringTypeService stringTypeService(StringContentMapper stringContentMapper) {
        StringTypeServiceImpl service = new StringTypeServiceImpl();
        service.setStringContentMapper(stringContentMapper);

        return service;
    }
}
