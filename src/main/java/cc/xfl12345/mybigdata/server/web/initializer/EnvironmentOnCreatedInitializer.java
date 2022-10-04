package cc.xfl12345.mybigdata.server.web.initializer;

import cc.xfl12345.mybigdata.server.common.appconst.AppConst;
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

    public static final String APP_LOGGING_FILE_PATH = "app.logging.file.path";

    public static final String SPRING_APPLICATION_NAME = "spring.application.name";

    public static final String LOGGING_FILE_NAME = "logging.file.name";

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

        String appName = environment.getProperty(SPRING_APPLICATION_NAME);
        if (appName == null || "".equals(appName)) {
            appName = AppConst.DEFAULT_APP_NAME;
            properties.setProperty(SPRING_APPLICATION_NAME, appName);
        }

        String fileNameBase = environment.getProperty(LOGGING_FILE_NAME);
        if (fileNameBase == null || "".equals(fileNameBase)) {
            fileNameBase = appName;
            properties.setProperty(LOGGING_FILE_NAME, fileNameBase);
        }

        String logBaseFolder = environment.getProperty("logging.file.path");
        if (logBaseFolder == null || "".equals(logBaseFolder)) {
            String home2LogPath = "/logs/" + appName + '/';
            // 判断是否在 Tomcat 容器下运行，决定 log 日志文件保存目录
            // org.apache.catalina.loader.ParallelWebappClassLoader
            String classLoaderName = Thread.currentThread().getContextClassLoader().getClass().getCanonicalName();
            if (classLoaderName.startsWith("org.apache.catalina") && System.getProperty("catalina.home") != null) {
                logBaseFolder = (new File(System.getProperty("catalina.home") + home2LogPath)).toString();
            } else {
                logBaseFolder = (new File(System.getProperty("user.home") + home2LogPath)).toString();
            }
        }

        properties.setProperty(APP_LOGGING_FILE_PATH, logBaseFolder);

        MutablePropertySources propertySources = environment.getPropertySources();
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("myDiyEnv", properties);
        propertySources.addFirst(propertiesPropertySource);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}

