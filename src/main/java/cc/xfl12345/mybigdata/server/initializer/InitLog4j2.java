package cc.xfl12345.mybigdata.server.initializer;

import cc.xfl12345.mybigdata.server.appconst.SpringAppLaunchMode;
import org.apache.commons.codec.Resources;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@Deprecated
public class InitLog4j2 {
    protected static void initLog4j(InputStream conf) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ConfigurationSource source = new ConfigurationSource(conf);
        LoggerContext loggerContext = Configurator.initialize(null, source);
        Logger logger = LogManager.getLogger(InitLog4j2.class);
        logger.info("Init Log4j2 as started state.");

        // 标记为 SpringBoot 认识的 已初始化，避免被 SpringBoot 重新读取文件重新初始化
        Log4J2LoggingSystem log4J2LoggingSystem = new Log4J2LoggingSystem(Thread.currentThread().getContextClassLoader());
        Method markAsInitialized = log4J2LoggingSystem.getClass().getDeclaredMethod("markAsInitialized", LoggerContext.class);
        markAsInitialized.setAccessible(true);
        markAsInitialized.invoke(log4J2LoggingSystem, loggerContext);
    }

    public static void init(SpringAppLaunchMode mode) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        InputStream inputStream = Resources.getInputStream("log/conf/tomcat/log4j2.xml");
        if(inputStream != null) {
            switch (mode) {
                case JAR -> {
                    //假装认为 代码块结束之后，string应该会被回收
                    {
                        Document xmlDoc = Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), "", Parser.xmlParser());
                        inputStream.close();
                        xmlDoc.getElementsByTag("configuration").get(0)
                            .getElementsByTag("Properties").get(0)
                            .getElementsByAttributeValue("name", "logBaseFolder")
                            .html("${sys:user.home}/logs/${APPNAME}");
                        inputStream = IOUtils.toInputStream(xmlDoc.html(), StandardCharsets.UTF_8);
                    }
                    initLog4j(inputStream);
                    LogManager.getLogger(InitLog4j2.class).info("Init Log4j2 in JAR mode succeed.");
                }
                case WAR -> {
                    initLog4j(inputStream);
                    LogManager.getLogger(InitLog4j2.class).info("Init Log4j2 in WAR mode succeed.");
                }
            }
            inputStream.close();
        }
    }
}
