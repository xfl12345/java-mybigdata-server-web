package cc.xfl12345.mybigdata.server.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class MybigdataApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybigdataApplication.class, args);
    }

}
