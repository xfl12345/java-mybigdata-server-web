package cc.xfl12345.mybigdata.server.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

@Configuration
public class EnvironmentOnCreated implements EnvironmentPostProcessor, Ordered {

    @SuppressWarnings("unchecked")
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String classLoaderName = Thread.currentThread().getContextClassLoader().getClass().getCanonicalName();

        Properties properties = new Properties();
        // org.apache.catalina.loader.ParallelWebappClassLoader
        if(classLoaderName.startsWith("org.apache.catalina")) {
            properties.setProperty("logging.config", "classpath:log/conf/tomcat/log4j2.xml");
        } else {
            properties.setProperty("logging.config", "classpath:log/conf/normal/log4j2.xml");
        }

        MutablePropertySources propertySources = environment.getPropertySources();
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("myDynamicLog4j2Config",properties);
        propertySources.addFirst(propertiesPropertySource);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

}

