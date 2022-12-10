package cc.xfl12345.mybigdata.server.web.config;

import cc.xfl12345.mybigdata.server.web.model.generator.RandomCodeGenerator;
import cc.xfl12345.mybigdata.server.web.model.generator.impl.RandomCodeGeneratorImpl;
import cc.xfl12345.mybigdata.server.web.pojo.RequestAnalyser;
import cc.xfl12345.mybigdata.server.web.pojo.ResourceCacheMapBean;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.apache.tika.Tika;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

    @Bean
    @ConditionalOnMissingBean
    public Tika tika() {
        Tika tika = new Tika();
        tika.setMaxStringLength(Integer.MAX_VALUE);
        return tika;
    }

    @Bean
    @ConditionalOnMissingBean
    public RandomCodeGenerator randomCodeGenerator() {
        return new RandomCodeGeneratorImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestAnalyser requestAnalyser() {
        return new RequestAnalyser();
    }
}
