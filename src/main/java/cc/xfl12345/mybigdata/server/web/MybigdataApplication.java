package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.web.appconst.SpringAppLaunchMode;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@EnableConfigurationProperties
@SpringBootApplication
public class MybigdataApplication {
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringAppStatus.launchMode = SpringAppLaunchMode.JAR;
        context = SpringApplication.run(MybigdataApplication.class, args);
    }

    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(MybigdataApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
}
