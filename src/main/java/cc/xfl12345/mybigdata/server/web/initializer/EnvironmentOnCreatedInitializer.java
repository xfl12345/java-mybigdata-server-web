package cc.xfl12345.mybigdata.server.web.initializer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Properties;

@Component
public class EnvironmentOnCreatedInitializer implements EnvironmentPostProcessor, Ordered {
    public static final String APP_LOGGING_CONSOLE_LEVEL = "app.logging.console.level";

    @SuppressWarnings("unchecked")
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Properties properties = new Properties();

        boolean debugMode = Boolean.parseBoolean(environment.getProperty("debug"));
        if (debugMode) {
            properties.setProperty(APP_LOGGING_CONSOLE_LEVEL, "DEBUG");
        } else {
            if (environment.getProperty(APP_LOGGING_CONSOLE_LEVEL) == null) {
                properties.setProperty(APP_LOGGING_CONSOLE_LEVEL, "INFO");
            }
        }

        String appName = environment.getProperty("spring.application.name");

        String classLoaderName = Thread.currentThread().getContextClassLoader().getClass().getCanonicalName();
        String logBaseFolder = null;
        String home2LogPath = "/logs/" + appName + '/';
        // 判断是否在 Tomcat 容器下运行，决定 log 日志文件保存目录
        // org.apache.catalina.loader.ParallelWebappClassLoader
        if (classLoaderName.startsWith("org.apache.catalina") && System.getProperty("catalina.home") != null) {
            logBaseFolder = (new File(System.getProperty("catalina.home") + home2LogPath)).toString();
        } else {
            logBaseFolder = (new File(System.getProperty("user.home") + home2LogPath)).toString();
        }
        properties.setProperty("app.logging.file.path", logBaseFolder);

        MutablePropertySources propertySources = environment.getPropertySources();
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("myDynamicLogConfig", properties);
        propertySources.addFirst(propertiesPropertySource);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}

