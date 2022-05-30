package cc.xfl12345.mybigdata.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Slf4j
@Configuration
public class JdbcConfig {

    @DependsOn("myDatabaseInitializer")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean("dataSource")
    public DruidDataSource getDruidDataSource() {
        return new DruidDataSource();
    }


    @DependsOn("dataSource")
    @Bean("nutzDao")
    public Dao getNutzDao() {
        return new NutDao(getDruidDataSource());
    }


}
