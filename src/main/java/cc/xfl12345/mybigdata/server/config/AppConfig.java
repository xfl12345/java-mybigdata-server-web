package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorAnalyst;
import cc.xfl12345.mybigdata.server.model.database.error.impl.SqlErrorAnalystImpl;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.CoreTableCache;
import cc.xfl12345.mybigdata.server.model.generator.RandomCodeGenerator;
import cc.xfl12345.mybigdata.server.model.generator.impl.RandomCodeGeneratorImpl;
import cc.xfl12345.mybigdata.server.model.transaction.impl.TransactionFactory;
import cc.xfl12345.mybigdata.server.model.transaction.impl.TransactionParam;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@AutoConfigureAfter({ IndependenceBeansConfig.class, JdbcConfig.class })
public class AppConfig {
    @Bean("transactionFactory")
    @ConditionalOnMissingBean(TransactionFactory.class)
    public TransactionFactory getTransactionFactory() throws Exception {
        TransactionFactory transactionFactory = new TransactionFactory();
        transactionFactory.setDefaultTransactionParam(
            TransactionParam.Builder.aBuilder()
                // .withTransactionIsolationLevel(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ.getLevel())
                .build()
        );
        return transactionFactory;
    }

    @Bean(name = "coreTableCache")
    @DependsOn("mongoDatastore")
    public CoreTableCache getCoreTableCache() throws Exception {
        CoreTableCache coreTableCache = new CoreTableCache();
        coreTableCache.setTransactionFactory(getTransactionFactory());
        return coreTableCache;
    }

    @Bean("sqlErrorHandler")
    @ConditionalOnMissingBean(SqlErrorAnalyst.class)
    public SqlErrorAnalyst getSqlErrorHandler() {
        return new SqlErrorAnalystImpl();
    }

    @Bean("randomCodeGenerator")
    @ConditionalOnMissingBean(RandomCodeGenerator.class)
    public RandomCodeGenerator getRandomCodeGenerator() {
        return new RandomCodeGeneratorImpl();
    }
}
