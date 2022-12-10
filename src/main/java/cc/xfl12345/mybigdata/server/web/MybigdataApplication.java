package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.web.appconst.SpringAppLaunchMode;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import static cc.xfl12345.mybigdata.server.web.SpringAppStatus.restartCount;

@EnableConfigurationProperties
@SpringBootApplication
public class MybigdataApplication {
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) throws Exception {
        SpringAppStatus.launchMode = SpringAppLaunchMode.JAR;
        try {
            SpringAppOuterHook.beforeAppStarted();
            context = SpringApplication.run(MybigdataApplication.class, args);
            SpringAppOuterHook.afterAppStarted(context);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }  catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(MybigdataApplication.class, args.getSourceArgs());
        });

        restartCount += 1;
        thread.setDaemon(false);
        thread.setName("restart-" + restartCount);
        thread.start();
    }
}
