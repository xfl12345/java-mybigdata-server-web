package cc.xfl12345.mybigdata.server.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Slf4j
@Configuration
public class DruidConfig {

    @DependsOn("myDatabaseInitializer")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource getDruidDataSource() {
        return new DruidDataSource();
    }
}
