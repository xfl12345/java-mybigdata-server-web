package cc.xfl12345.mybigdata.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

@EnableConfigurationProperties
@SpringBootApplication
public class MybigdataApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MybigdataApplication.class, args);

//        log.info("启动成功：Sa-Token配置如下：" + SaManager.getConfig());
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            System.out.println("App stopped,bye bye!");
//        }));
    }

}
