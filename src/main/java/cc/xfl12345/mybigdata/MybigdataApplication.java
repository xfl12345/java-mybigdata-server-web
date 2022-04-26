package cc.xfl12345.mybigdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

@EnableConfigurationProperties
@ImportResource(value = {"${spring-xml}"})
@SpringBootApplication
public class MybigdataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybigdataApplication.class, args);
    }

}
