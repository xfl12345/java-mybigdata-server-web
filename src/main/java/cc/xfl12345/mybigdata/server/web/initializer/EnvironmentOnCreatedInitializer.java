package cc.xfl12345.mybigdata.server.web.initializer;

import cc.xfl12345.mybigdata.server.common.appconst.AppConst;
import cc.xfl12345.mybigdata.server.common.utility.ConsoleCharsetUtils;
import cc.xfl12345.mybigdata.server.web.SpringAppOuterHook;
import cc.xfl12345.mybigdata.server.web.appconst.EnvConst;
import cc.xfl12345.mybigdata.server.web.gui.SpringAppConsoleGUI;
import cc.xfl12345.mybigdata.server.web.utility.SynchronizeLock;
import org.fusesource.jansi.AnsiConsole;
import org.fusesource.jansi.AnsiType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

public class EnvironmentOnCreatedInitializer implements EnvironmentPostProcessor, Ordered {
    public static final SynchronizeLock waitGuiLock = new SynchronizeLock();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Properties properties = new Properties();

        boolean isTomcatLauncherThread = isTomcatLauncherThread();
        boolean appGuiEnable = Boolean.parseBoolean(environment.getProperty(EnvConst.APP_GUI_ENABLED));
        // 如果 GUI 模式 且 不是以 Tomcat 容器启动 （后者是必判选项，所以优先判断后者，可减少一次判断）
        if (!isTomcatLauncherThread && appGuiEnable) {
            // 覆盖 Spring ANSI 配置
            properties.setProperty(EnvConst.SPRING_OUTPUT_ANSI_ENABLED, AnsiOutput.Enabled.NEVER.name().toLowerCase(Locale.ROOT));
            // 卸载 jansi
            if (AnsiConsole.isInstalled()) {
                AnsiConsole.systemUninstall();
            }

            // 启动 GUI
            Thread guiLaunchThread = new Thread(() -> {
                SpringAppConsoleGUI.main(new String[0]);
            });
            guiLaunchThread.setName("GUI Launch Thread");
            guiLaunchThread.start();

            try {
                waitGuiLock.justSynchronize();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            String springOutputAnsiEnabled = environment.getProperty(EnvConst.SPRING_OUTPUT_ANSI_ENABLED);
            // 如果没有配置
            if (springOutputAnsiEnabled == null || "".equals(springOutputAnsiEnabled)) {
                // 配置 Spring ANSI 为 always ，因为 jansi 会自动处理终端不支持的情况。
                properties.setProperty(EnvConst.SPRING_OUTPUT_ANSI_ENABLED, AnsiOutput.Enabled.ALWAYS.name().toLowerCase(Locale.ROOT));
                AnsiConsole.systemInstall();
            } else {
                // 如果不是 NEVER 选项
                if (!springOutputAnsiEnabled.toUpperCase(Locale.ROOT).equals(AnsiOutput.Enabled.NEVER.name().toUpperCase(Locale.ROOT))) {
                    AnsiConsole.systemInstall();
                }
            }
        }

        boolean debugMode = Boolean.parseBoolean(environment.getProperty("debug"));
        if (debugMode) {
            properties.setProperty(EnvConst.APP_LOGGING_CONSOLE_LEVEL, "DEBUG");
        } else {
            String appLoggingConsoleLevel = environment.getProperty(EnvConst.APP_LOGGING_CONSOLE_LEVEL);
            if (appLoggingConsoleLevel == null || "".equals(appLoggingConsoleLevel)) {
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
            // 如果是 GUI 窗口输出 log
            if (appGuiEnable) {
                System.out.println("GUI mode detected.");
                try {
                    charset = Charset.defaultCharset();
                } catch (Exception e) {
                    // ignore
                }
            } else {
                ConsoleCharsetUtils consoleCharsetUtils = SpringAppOuterHook.getSingletonByClass(ConsoleCharsetUtils.class);
                if (consoleCharsetUtils != null) {
                    charset = consoleCharsetUtils.getCharset();
                }
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
            if (isTomcatLauncherThread) {
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

    public boolean isTomcatLauncherThread() {
        // org.apache.catalina.loader.ParallelWebappClassLoader
        String classLoaderName = Thread.currentThread().getContextClassLoader().getClass().getCanonicalName();
        return classLoaderName != null && classLoaderName.startsWith("org.apache.catalina") && System.getProperty("catalina.home") != null;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}

