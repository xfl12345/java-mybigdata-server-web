package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.generator.RandomCodeGenerator;
import cc.xfl12345.mybigdata.server.model.generator.impl.RandomCodeGeneratorImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    @ConditionalOnMissingBean
    public RandomCodeGenerator randomCodeGenerator() {
        return new RandomCodeGeneratorImpl();
    }
}
