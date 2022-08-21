package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.table.curd.*;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.impl.GlobalDataRecordHandlerImpl;
import cc.xfl12345.mybigdata.server.model.database.table.curd.orm.config.BeeOrmTableHandlerConfig;
import cc.xfl12345.mybigdata.server.model.database.table.curd.orm.config.BeeOrmTableHandlerConfigGenerator;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;

@Configuration
public class MapperConfig {
    @Bean
    public <TablePojoType> BeeOrmTableHandlerConfig<TablePojoType> getHandlerConfig(Class<TablePojoType> cls)
        throws NoSuchMethodException {
        return BeeOrmTableHandlerConfigGenerator.getConfig(cls);
    }

    @SuppressWarnings("unchecked")
    public <T> AbstractAppTableHandler<T> getHandler(Class<T> cls)
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<AbstractAppTableHandler<T>> handlerClass = (Class<AbstractAppTableHandler<T>>) Class.forName(
            GlobalDataRecordHandlerImpl.class.getPackageName() + "." + cls.getSimpleName() + "HandlerImpl"
        );
        AbstractAppTableHandler<T> handler = handlerClass.getDeclaredConstructor().newInstance();
        handler.setHandlerConfig(getHandlerConfig(cls));
        return handler;
    }

    @Bean
    @ConditionalOnMissingBean(AuthAccountHandler.class)
    public AuthAccountHandler getAuthAccountHandler() throws Exception {
        return (AuthAccountHandler) getHandler(AuthAccount.class);
    }

    @Bean
    @ConditionalOnMissingBean(GlobalDataRecordHandler.class)
    public GlobalDataRecordHandler getGlobalDataRecordHandler() throws Exception {
        return (GlobalDataRecordHandler) getHandler(GlobalDataRecord.class);
    }

    @Bean
    @ConditionalOnMissingBean(GroupContentHandler.class)
    public GroupContentHandler getGroupContentHandler() throws Exception {
        return (GroupContentHandler) getHandler(GroupContent.class);
    }

    @Bean
    @ConditionalOnMissingBean(GroupRecordHandler.class)
    public GroupRecordHandler getGroupRecordHandler() throws Exception {
        return (GroupRecordHandler) getHandler(GroupRecord.class);
    }

    @Bean
    @ConditionalOnMissingBean(NumberContentHandler.class)
    public NumberContentHandler getNumberContentHandler() throws Exception {
        return (NumberContentHandler) getHandler(NumberContent.class);
    }

    @Bean
    @ConditionalOnMissingBean(ObjectContentHandler.class)
    public ObjectContentHandler getObjectContentHandler() throws Exception {
        return (ObjectContentHandler) getHandler(ObjectContent.class);
    }

    @Bean
    @ConditionalOnMissingBean(ObjectRecordHandler.class)
    public ObjectRecordHandler getObjectRecordHandler() throws Exception {
        return (ObjectRecordHandler) getHandler(ObjectRecord.class);
    }

    @Bean
    @ConditionalOnMissingBean(StringContentHandler.class)
    public StringContentHandler getStringContentHandler() throws Exception {
        return (StringContentHandler) getHandler(StringContent.class);
    }

    @Bean
    @ConditionalOnMissingBean(TableSchemaRecordHandler.class)
    public TableSchemaRecordHandler getTableSchemaRecordHandler() throws Exception {
        return (TableSchemaRecordHandler) getHandler(TableSchemaRecord.class);
    }
}
