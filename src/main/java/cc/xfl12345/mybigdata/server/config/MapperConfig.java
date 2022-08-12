package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.table.curd.*;
import cc.xfl12345.mybigdata.server.model.database.table.curd.impl.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    @ConditionalOnMissingBean(AuthAccountHandler.class)
    public AuthAccountHandler getAuthAccountHandler() {
        return new AuthAccountHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(GlobalDataRecordHandler.class)
    public GlobalDataRecordHandler getGlobalDataRecordHandler() {
        return new GlobalDataRecordHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(GroupContentHandler.class)
    public GroupContentHandler getGroupContentHandler() {
        return new GroupContentHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(GroupRecordHandler.class)
    public GroupRecordHandler getGroupRecordHandler() {
        return new GroupRecordHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(NumberContentHandler.class)
    public NumberContentHandler getIntegerContentHandler() {
        return new NumberContentHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(ObjectContentHandler.class)
    public ObjectContentHandler getObjectContentHandler() {
        return new ObjectContentHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(ObjectRecordHandler.class)
    public ObjectRecordHandler getObjectRecordHandler() {
        return new ObjectRecordHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(StringContentHandler.class)
    public StringContentHandler getStringContentHandler() {
        return new StringContentHandlerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(TableSchemaRecordHandler.class)
    public TableSchemaRecordHandler getTableSchemaRecordHandler() {
        return new TableSchemaRecordHandlerImpl();
    }
}
