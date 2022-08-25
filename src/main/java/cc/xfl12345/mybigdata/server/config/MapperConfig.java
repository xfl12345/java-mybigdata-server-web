package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.table.curd.*;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.impl.GlobalDataRecordMapperImpl;
import cc.xfl12345.mybigdata.server.model.database.table.curd.impl.orm.config.BeeOrmTableMapperConfig;
import cc.xfl12345.mybigdata.server.model.database.table.curd.impl.orm.config.BeeOrmTableMapperConfigGenerator;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;

@Configuration
public class MapperConfig {
    @Bean
    public <TablePojoType> BeeOrmTableMapperConfig<TablePojoType> getHandlerConfig(Class<TablePojoType> cls)
        throws NoSuchMethodException {
        return BeeOrmTableMapperConfigGenerator.getConfig(cls);
    }

    @SuppressWarnings("unchecked")
    public <T> AbstractAppTableMapper<T> getMapper(Class<T> cls)
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<AbstractAppTableMapper<T>> mapperClass = (Class<AbstractAppTableMapper<T>>) Class.forName(
            GlobalDataRecordMapperImpl.class.getPackageName() + "." + cls.getSimpleName() + "MapperImpl"
        );
        AbstractAppTableMapper<T> handler = mapperClass.getDeclaredConstructor().newInstance();
        handler.setMapperConfig(getHandlerConfig(cls));
        return handler;
    }

    @Bean
    @ConditionalOnMissingBean(AuthAccountMapper.class)
    public AuthAccountMapper getAuthAccountMapper() throws Exception {
        return (AuthAccountMapper) getMapper(AuthAccount.class);
    }

    @Bean
    @ConditionalOnMissingBean(GlobalDataRecordMapper.class)
    public GlobalDataRecordMapper getGlobalDataRecordMapper() throws Exception {
        return (GlobalDataRecordMapper) getMapper(GlobalDataRecord.class);
    }

    @Bean
    @ConditionalOnMissingBean(GroupContentMapper.class)
    public GroupContentMapper getGroupContentMapper() throws Exception {
        return (GroupContentMapper) getMapper(GroupContent.class);
    }

    @Bean
    @ConditionalOnMissingBean(GroupRecordMapper.class)
    public GroupRecordMapper getGroupRecordMapper() throws Exception {
        return (GroupRecordMapper) getMapper(GroupRecord.class);
    }

    @Bean
    @ConditionalOnMissingBean(NumberContentMapper.class)
    public NumberContentMapper getNumberContentMapper() throws Exception {
        return (NumberContentMapper) getMapper(NumberContent.class);
    }

    @Bean
    @ConditionalOnMissingBean(ObjectContentMapper.class)
    public ObjectContentMapper getObjectContentMapper() throws Exception {
        return (ObjectContentMapper) getMapper(ObjectContent.class);
    }

    @Bean
    @ConditionalOnMissingBean(ObjectRecordMapper.class)
    public ObjectRecordMapper getObjectRecordMapper() throws Exception {
        return (ObjectRecordMapper) getMapper(ObjectRecord.class);
    }

    @Bean
    @ConditionalOnMissingBean(StringContentMapper.class)
    public StringContentMapper getStringContentMapper() throws Exception {
        return (StringContentMapper) getMapper(StringContent.class);
    }

    @Bean
    @ConditionalOnMissingBean(TableSchemaRecordMapper.class)
    public TableSchemaRecordMapper getTableSchemaRecordMapper() throws Exception {
        return (TableSchemaRecordMapper) getMapper(TableSchemaRecord.class);
    }
}
