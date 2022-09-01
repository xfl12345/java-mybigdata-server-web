package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.pojo.ResourceCacheMapBean;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

@Configuration
public class IndependenceBeansConfig {
    @Bean
    public SimpleTimeZone defaultTimeZone() {
        return new SimpleTimeZone(28800000, "China Standard Time");
    }

    @Bean
    public SimpleDateFormat defaultDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Bean
    public SimpleDateFormat millisecondFormatter() {
        return new SimpleDateFormat("SSS");
    }

    @Bean
    public SimpleDateFormat fullDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Bean
    public TimeBasedGenerator uuidGenerator() {
        return Generators.timeBasedGenerator();
    }

    @Bean
    public ResourceCacheMapBean resourceCacheMapBean() {
        return new ResourceCacheMapBean();
    }
}
