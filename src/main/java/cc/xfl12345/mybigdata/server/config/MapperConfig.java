package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorAnalyst;
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
    public <T> AbstractAppTableHandler<T> getHandler(Class<T> cls, SqlErrorAnalyst sqlErrorAnalyst)
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<AbstractAppTableHandler<T>> handlerClass = (Class<AbstractAppTableHandler<T>>) Class.forName(
            GlobalDataRecordHandlerImpl.class.getPackageName() + "." + cls.getSimpleName() + "HandlerImpl"
        );
        AbstractAppTableHandler<T> handler = handlerClass.getDeclaredConstructor().newInstance();
        handler.setHandlerConfig(getHandlerConfig(cls));
        handler.setSqlErrorAnalyst(sqlErrorAnalyst);
        return handler;
    }

    @Bean
    @ConditionalOnMissingBean(AuthAccountHandler.class)
    public AuthAccountHandler getAuthAccountHandler(SqlErrorAnalyst sqlErrorAnalyst) throws Exception {
        return (AuthAccountHandler) getHandler(AuthAccount.class, sqlErrorAnalyst);
    }

    @Bean
    @ConditionalOnMissingBean(GlobalDataRecordHandler.class)
    public GlobalDataRecordHandler getGlobalDataRecordHandler(SqlErrorAnalyst sqlErrorAnalyst) throws Exception {
        return (GlobalDataRecordHandler) getHandler(GlobalDataRecord.class, sqlErrorAnalyst);
    }

    @Bean
    @ConditionalOnMissingBean(GroupContentHandler.class)
    public GroupContentHandler getGroupContentHandler(SqlErrorAnalyst sqlErrorAnalyst) throws Exception {
        return (GroupContentHandler) getHandler(GroupContent.class, sqlErrorAnalyst);
    }

    @Bean
    @ConditionalOnMissingBean(GroupRecordHandler.class)
    public GroupRecordHandler getGroupRecordHandler(SqlErrorAnalyst sqlErrorAnalyst) throws Exception {
        return (GroupRecordHandler) getHandler(GroupRecord.class, sqlErrorAnalyst);
    }

    @Bean
    @ConditionalOnMissingBean(NumberContentHandler.class)
    public NumberContentHandler getNumberContentHandler(SqlErrorAnalyst sqlErrorAnalyst) throws Exception {
        return (NumberContentHandler) getHandler(NumberContent.class, sqlErrorAnalyst);
    }

    @Bean
    @ConditionalOnMissingBean(ObjectContentHandler.class)
    public ObjectContentHandler getObjectContentHandler(SqlErrorAnalyst sqlErrorAnalyst) throws Exception {
        return (ObjectContentHandler) getHandler(ObjectContent.class, sqlErrorAnalyst);
    }

    @Bean
    @ConditionalOnMissingBean(ObjectRecordHandler.class)
    public ObjectRecordHandler getObjectRecordHandler(SqlErrorAnalyst sqlErrorAnalyst) throws Exception {
        return (ObjectRecordHandler) getHandler(ObjectRecord.class, sqlErrorAnalyst);
    }

    @Bean
    @ConditionalOnMissingBean(StringContentHandler.class)
    public StringContentHandler getStringContentHandler(SqlErrorAnalyst sqlErrorAnalyst) throws Exception {
        return (StringContentHandler) getHandler(StringContent.class, sqlErrorAnalyst);
    }

    @Bean
    @ConditionalOnMissingBean(TableSchemaRecordHandler.class)
    public TableSchemaRecordHandler getTableSchemaRecordHandler(SqlErrorAnalyst sqlErrorAnalyst) throws Exception {
        return (TableSchemaRecordHandler) getHandler(TableSchemaRecord.class, sqlErrorAnalyst);
    }
}
