package cc.xfl12345.mybigdata.server.model;

import com.fasterxml.uuid.Generators;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Objects;

@Component("staticSpringApp")
public class StaticSpringApp implements ApplicationContextAware {
    public static ConfigurableApplicationContext springAppContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springAppContext = (ConfigurableApplicationContext) Objects.requireNonNull(applicationContext);
    }

    public static ApplicationContext getApplicationContext() {
        return springAppContext;
    }

    public static Object getBean(String name) throws BeansException {
        return springAppContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return springAppContext.getBean(name, requiredType);
    }

    public static Object getBean(String name, Object... args) throws BeansException {
        return springAppContext.getBean(name, args);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return springAppContext.getBean(requiredType);
    }

    public static <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return springAppContext.getBean(requiredType, args);
    }

    public static SqlSessionFactory getSqlSessionFactory() throws Exception {
        return springAppContext.getBean("sqlSessionFactory", SqlSessionFactory.class);
    }

    public static SqlSession getSqlSession() throws Exception {
        return getSqlSessionFactory().openSession(true);
    }

    public static String getUUID() {
        return Generators.timeBasedGenerator().generate().toString();
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        return springAppContext.getBean("defaultDateFormat", SimpleDateFormat.class);
    }

    public static SimpleDateFormat getMillisecondFormatter() {
        return springAppContext.getBean("millisecondFormatter", SimpleDateFormat.class);
    }
}
