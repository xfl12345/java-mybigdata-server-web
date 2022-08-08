package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.error.impl.SqlErrorHandlerImpl;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.CoreTableCache;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.StringTypeHandlerImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@AutoConfigureAfter({ IndependenceBeansConfig.class, JdbcConfig.class })
public class AppConfig {
    @Getter
    protected IndependenceBeansConfig independenceBeansConfig;

    @Autowired
    public void setIndependenceBeansConfig(IndependenceBeansConfig independenceBeansConfig) {
        this.independenceBeansConfig = independenceBeansConfig;
    }

    @Getter
    protected JdbcConfig jdbcConfig;

    @Autowired
    public void setJdbcConfig(JdbcConfig jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
    }

    @Bean(name = "coreTableCache")
    @DependsOn("mongoDatastore")
    public CoreTableCache getCoreTableCache() throws Exception {
        return new CoreTableCache();
    }


    @Bean("sqlErrorHandler")
    public SqlErrorHandler getSqlErrorHandler() {
        return new SqlErrorHandlerImpl();
    }

    @Bean("stringTypeHandler")
    public StringTypeHandler getStringTypeHandler() throws Exception {
        StringTypeHandlerImpl stringTypeHandler = new StringTypeHandlerImpl();
        stringTypeHandler.setSqlErrorHandler(getSqlErrorHandler());
        stringTypeHandler.setUuidGenerator(independenceBeansConfig.getTimeBasedGenerator());
        stringTypeHandler.setCoreTableCache(getCoreTableCache());
        return stringTypeHandler;
    }
}
