package cc.xfl12345.mybigdata.server.web.initializer;

import cc.xfl12345.mybigdata.server.common.appconst.AppConst;
import cc.xfl12345.mybigdata.server.common.utility.ConsoleCharsetUtils;
import cc.xfl12345.mybigdata.server.web.SpringAppOuterHook;
import cc.xfl12345.mybigdata.server.web.appconst.EnvConst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EnvironmentOnCreatedInitializer implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Properties properties = new Properties();

        boolean debugMode = Boolean.parseBoolean(environment.getProperty("debug"));
        if (debugMode) {
            properties.setProperty(EnvConst.APP_LOGGING_CONSOLE_LEVEL, "DEBUG");
        } else {
            if (environment.getProperty(EnvConst.APP_LOGGING_CONSOLE_LEVEL) == null) {
                properties.setProperty(EnvConst.APP_LOGGING_CONSOLE_LEVEL, "INFO");
            }
        }

        String appName = environment.getProperty(EnvConst.SPRING_APPLICATION_NAME);
        if (appName == null || "".equals(appName)) {
            appName = AppConst.DEFAULT_APP_NAME;
            properties.setProperty(EnvConst.SPRING_APPLICATION_NAME, appName);
        }

        String fileNameBase = environment.getProperty(EnvConst.LOGGING_FILE_NAME);
        if (fileNameBase == null || "".equals(fileNameBase)) {
            fileNameBase = appName;
            properties.setProperty(EnvConst.LOGGING_FILE_NAME, fileNameBase);
        }

        String loggingCharsetConsole = environment.getProperty(EnvConst.LOGGING_CHARSET_CONSOLE);
        if ((loggingCharsetConsole == null || "".equals(loggingCharsetConsole))) {
            Charset charset = null;
            ConsoleCharsetUtils consoleCharsetUtils = SpringAppOuterHook.getSingletonByClass(ConsoleCharsetUtils.class);
            if (consoleCharsetUtils != null) {
                charset = consoleCharsetUtils.getCharset();
            }
            if (charset != null) {
                System.out.println("Current console charset name is [" + charset.name() + "]");
            } else {
                System.out.println("Retrieve environment charset failed. Using 'UTF-8' as default.");
                charset = StandardCharsets.UTF_8;
            }
            properties.setProperty(EnvConst.LOGGING_CHARSET_CONSOLE, charset.name());
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

        properties.setProperty(EnvConst.APP_LOGGING_FILE_PATH, logBaseFolder);

        MutablePropertySources propertySources = environment.getPropertySources();
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("myDiyEnv", properties);
        propertySources.addFirst(propertiesPropertySource);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}

