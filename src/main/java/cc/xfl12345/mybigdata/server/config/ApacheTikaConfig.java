package cc.xfl12345.mybigdata.server.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApacheTikaConfig {

    @Bean(name = "tika")
    public Tika getTika() {
        Tika tika = new Tika();
        tika.setMaxStringLength(Integer.MAX_VALUE);
        return tika;
    }

}
