package cc.xfl12345.mybigdata.server.web.initializer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class EnvironmentOnCreatedInitializer implements EnvironmentPostProcessor, Ordered {

    @SuppressWarnings("unchecked")
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String classLoaderName = Thread.currentThread().getContextClassLoader().getClass().getCanonicalName();
        Properties properties = new Properties();
        // 判断是否在 Tomcat 容器下运行，决定 log 日志文件保存目录
        // org.apache.catalina.loader.ParallelWebappClassLoader
        if(classLoaderName.startsWith("org.apache.catalina")) {
            properties.setProperty("logging.config", "classpath:log/conf/tomcat/logback-spring.xml");
        } else {
            properties.setProperty("logging.config", "classpath:log/conf/normal/logback-spring.xml");
        }

        MutablePropertySources propertySources = environment.getPropertySources();
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("myDynamicLogConfig",properties);
        propertySources.addFirst(propertiesPropertySource);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}

