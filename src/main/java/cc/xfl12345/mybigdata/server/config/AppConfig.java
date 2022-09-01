package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorAnalyst;
import cc.xfl12345.mybigdata.server.model.database.error.impl.SqlErrorAnalystImpl;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.CoreTableCache;
import cc.xfl12345.mybigdata.server.model.generator.RandomCodeGenerator;
import cc.xfl12345.mybigdata.server.model.generator.impl.RandomCodeGeneratorImpl;
import cc.xfl12345.mybigdata.server.model.transaction.impl.TransactionFactory;
import cc.xfl12345.mybigdata.server.model.transaction.impl.TransactionParam;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;

@Configuration
public class AppConfig {
    @Bean
    @ConditionalOnMissingBean
    public TransactionFactory transactionFactory() throws Exception {
        TransactionFactory transactionFactory = new TransactionFactory();
        transactionFactory.setDefaultTransactionParam(
            TransactionParam.Builder.aBuilder()
                .withTransactionIsolationLevel(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ.getLevel())
                .build()
        );
        return transactionFactory;
    }

    @DependsOn("sessionFactory")
    @Bean
    @ConditionalOnMissingBean
    public CoreTableCache coreTableCache(TransactionFactory transactionFactory) throws Exception {
        CoreTableCache coreTableCache = new CoreTableCache();
        coreTableCache.setTransactionFactory(transactionFactory);
        return coreTableCache;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlErrorAnalyst sqlErrorAnalyst() {
        return new SqlErrorAnalystImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public RandomCodeGenerator randomCodeGenerator() {
        return new RandomCodeGeneratorImpl();
    }


}
