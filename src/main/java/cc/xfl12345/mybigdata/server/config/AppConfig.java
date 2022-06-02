package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.CoreTableCache;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.StringTypeHandlerImpl;
import cc.xfl12345.mybigdata.server.model.database.producer.GlobalDataRecordProducer;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.SessionFactory;
import org.teasoft.spring.boot.config.BeeAutoConfiguration;
import org.teasoft.spring.boot.config.BeeManageConfig;
import org.teasoft.spring.boot.config.BeeXmlConfiguration;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter({ IndependenceBeansConfig.class, JdbcConfig.class, BeeAutoConfiguration.class })
public class AppConfig {
    // @Getter
    // protected ApplicationContext applicationContext;
    //
    // @Autowired
    // public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
    //     this.applicationContext = applicationContext;
    // }

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
    public BeeFactory getBeeFactory() {
        BeeFactory beeFactory = BeeFactory.getInstance();
        beeFactory.setDataSource(jdbcConfig.getDruidDataSource());
        return beeFactory;
    }

    @Bean("sessionFactory")
    public SessionFactory getSessionFactory() {
        SessionFactory factory = new SessionFactory();
        factory.setBeeFactory(getBeeFactory());
        return factory;
    }

    @Bean(name = "coreTableCache")
    @DependsOn("sessionFactory")
    public CoreTableCache getCoreTableCache() throws Exception {
        return new CoreTableCache();
    }


    @Bean("globalDataRecordProducer")
    public GlobalDataRecordProducer getGlobalDataRecordProducer() {
        return new GlobalDataRecordProducer(independenceBeansConfig.getTimeBasedGenerator());
    }

    @Bean("stringTypeHandler")
    public StringTypeHandler getStringTypeHandler() throws Exception {
        StringTypeHandlerImpl stringTypeHandler = new StringTypeHandlerImpl();
        stringTypeHandler.setCoreTableCache(getCoreTableCache());
        stringTypeHandler.setGlobalDataRecordProducer(getGlobalDataRecordProducer());
        return stringTypeHandler;
    }
}
