package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.error.impl.SqlErrorHandlerImpl;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.CoreTableCache;
import cc.xfl12345.mybigdata.server.model.generator.RandomCodeGenerator;
import cc.xfl12345.mybigdata.server.model.generator.impl.RandomCodeGeneratorImpl;
import cc.xfl12345.mybigdata.server.model.transaction.impl.TransactionFactory;
import cc.xfl12345.mybigdata.server.model.transaction.impl.TransactionParam;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;
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
    @Bean("transactionFactory")
    @ConditionalOnMissingBean(TransactionFactory.class)
    public TransactionFactory getTransactionFactory() throws Exception {
        TransactionFactory transactionFactory = new TransactionFactory();
        transactionFactory.setDefaultTransactionParam(
            TransactionParam.Builder.aBuilder()
                .withTransactionIsolationLevel(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ.getLevel())
                .build()
        );
        return transactionFactory;
    }

    @DependsOn("sessionFactory")
    @Bean("coreTableCache")
    @ConditionalOnMissingBean(CoreTableCache.class)
    public CoreTableCache getCoreTableCache() throws Exception {
        CoreTableCache coreTableCache = new CoreTableCache();
        coreTableCache.setTransactionFactory(getTransactionFactory());
        return coreTableCache;
    }

    @Bean("sqlErrorHandler")
    @ConditionalOnMissingBean(SqlErrorHandler.class)
    public SqlErrorHandler getSqlErrorHandler() {
        return new SqlErrorHandlerImpl();
    }

    @Bean("randomCodeGenerator")
    @ConditionalOnMissingBean(RandomCodeGenerator.class)
    public RandomCodeGenerator getRandomCodeGenerator() {
        return new RandomCodeGeneratorImpl();
    }


}
