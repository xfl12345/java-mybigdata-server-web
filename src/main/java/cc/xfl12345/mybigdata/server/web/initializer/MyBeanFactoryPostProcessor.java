package cc.xfl12345.mybigdata.server.web.initializer;


import cc.xfl12345.mybigdata.server.web.appconst.EnvConst;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        ConfigurableEnvironment configurableEnvironment = configurableListableBeanFactory.getBean(ConfigurableEnvironment.class);
        log.info("Final console charset name is [" + configurableEnvironment.getProperty(EnvConst.LOGGING_CHARSET_CONSOLE) + "]");
        // 优先初始化一些 Bean
        // Something else
    }
}
