package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.CoreTableCache;
import cc.xfl12345.mybigdata.server.model.database.error.impl.SqlErrorHandlerImpl;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.StringTypeHandlerImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.SessionFactory;
import org.teasoft.spring.boot.config.BeeAutoConfiguration;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter({ IndependenceBeansConfig.class, JdbcConfig.class, BeeAutoConfiguration.class })
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

    @Bean("beeFactory")
    @ConditionalOnMissingBean(BeeFactory.class)
    @ConditionalOnSingleCandidate(DataSource.class)
    public BeeFactory getBeeFactory() {
        BeeFactory beeFactory = BeeFactory.getInstance();
        beeFactory.setDataSource(jdbcConfig.getDruidDataSource());
        return beeFactory;
    }

    @Bean("sessionFactory")
    @ConditionalOnMissingBean(SessionFactory.class)
    @ConditionalOnBean(BeeFactory.class)
    public SessionFactory getSessionFactory() {
        SessionFactory factory = new SessionFactory();
        factory.setBeeFactory(getBeeFactory());
        return factory;
    }

    @DependsOn("sessionFactory")
    @Bean(name = "coreTableCache")
    @ConditionalOnMissingBean(CoreTableCache.class)
    public CoreTableCache getCoreTableCache() throws Exception {
        return new CoreTableCache();
    }

    @Bean("sqlErrorHandler")
    @ConditionalOnMissingBean(SqlErrorHandler.class)
    public SqlErrorHandler getSqlErrorHandler() {
        return new SqlErrorHandlerImpl();
    }

    @Bean("stringTypeHandler")
    public StringTypeHandler getStringTypeHandler() throws Exception {
        return new StringTypeHandlerImpl();
    }
}
