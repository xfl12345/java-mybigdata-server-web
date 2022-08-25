package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.service.NumberTypeService;
import cc.xfl12345.mybigdata.server.model.database.service.StringTypeService;
import cc.xfl12345.mybigdata.server.model.database.service.impl.NumberTypeServiceImpl;
import cc.xfl12345.mybigdata.server.model.database.service.impl.StringTypeServiceImpl;
import cc.xfl12345.mybigdata.server.model.database.table.curd.NumberContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.StringContentMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({MapperConfig.class})
public class DataServiceConfig {
    @Bean
    public StringTypeService stringTypeService(StringContentMapper mapper) {
        StringTypeServiceImpl service = new StringTypeServiceImpl();
        service.setMapper(mapper);

        return service;
    }

    @Bean
    public NumberTypeService numberTypeService(NumberContentMapper mapper) {
        NumberTypeServiceImpl service = new NumberTypeServiceImpl();
        service.setMapper(mapper);

        return service;
    }
}
