package cc.xfl12345.mybigdata.server.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApacheTikaConfig {
    @Bean
    public Tika tika() {
        Tika tika = new Tika();
        tika.setMaxStringLength(Integer.MAX_VALUE);
        return tika;
    }

}
