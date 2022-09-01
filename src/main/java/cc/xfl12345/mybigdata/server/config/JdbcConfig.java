package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.initializer.MyDatabaseInitializer;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class JdbcConfig {
    @Bean
    @Scope("prototype")
    public MyDatabaseInitializer myDatabaseInitializer() {
        return new MyDatabaseInitializer();
    }

    @DependsOn("myDatabaseInitializer")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean("dataSource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean
    public Dao nutzDao(DataSource dataSource) {
        return new NutDao(dataSource);
    }
}
