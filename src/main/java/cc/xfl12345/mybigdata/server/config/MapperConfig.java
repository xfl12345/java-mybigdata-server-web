package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.table.converter.IdTypeConverter;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.*;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.CoreTableCache;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.impl.GlobalDataRecordMapperImpl;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.orm.bee.config.BeeTableMapperConfig;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.orm.bee.config.BeeTableMapperConfigGenerator;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.*;
import com.fasterxml.uuid.NoArgGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;

@Configuration
public class MapperConfig {

    protected NoArgGenerator uuidGenerator;

    @Autowired
    public void setUuidGenerator(NoArgGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    protected CoreTableCache coreTableCache;

    @Autowired
    public void setCoreTableCache(CoreTableCache coreTableCache) {
        this.coreTableCache = coreTableCache;
    }

    @Bean
    public IdTypeConverter<Long> idTypeConverter() {
        return new IdTypeConverter<>(Long.class);
    }

    @Bean
    public <TablePojoType> BeeTableMapperConfig<TablePojoType> getHandlerConfig(Class<TablePojoType> cls)
        throws NoSuchMethodException {
        return BeeTableMapperConfigGenerator.getConfig(cls);
    }

    @SuppressWarnings("unchecked")
    public <T> AbstractAppTableMapper<T> getMapper(Class<T> cls)
        throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<AbstractAppTableMapper<T>> mapperClass = (Class<AbstractAppTableMapper<T>>) Class.forName(
            GlobalDataRecordMapperImpl.class.getPackageName() + "." + cls.getSimpleName() + "MapperImpl"
        );
        AbstractAppTableMapper<T> mapper = mapperClass.getDeclaredConstructor().newInstance();
        mapper.setIdTypeConverter(idTypeConverter());
        mapper.setCoreTableCache(coreTableCache);
        mapper.setUuidGenerator(uuidGenerator);
        mapper.setMapperConfig(getHandlerConfig(cls));

        return mapper;
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthAccountMapper authAccountMapper() throws Exception {
        return (AuthAccountMapper) getMapper(AuthAccount.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalDataRecordMapper globalDataRecordMapper() throws Exception {
        return (GlobalDataRecordMapper) getMapper(GlobalDataRecord.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public GroupContentMapper groupContentMapper() throws Exception {
        return (GroupContentMapper) getMapper(GroupContent.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public GroupRecordMapper groupRecordMapper() throws Exception {
        return (GroupRecordMapper) getMapper(GroupRecord.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public NumberContentMapper numberContentMapper() throws Exception {
        return (NumberContentMapper) getMapper(NumberContent.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectContentMapper objectContentMapper() throws Exception {
        return (ObjectContentMapper) getMapper(ObjectContent.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectRecordMapper objectRecordMapper() throws Exception {
        return (ObjectRecordMapper) getMapper(ObjectRecord.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public StringContentMapper stringContentMapper() throws Exception {
        return (StringContentMapper) getMapper(StringContent.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public TableSchemaRecordMapper tableSchemaRecordMapper() throws Exception {
        return (TableSchemaRecordMapper) getMapper(TableSchemaRecord.class);
    }
}
