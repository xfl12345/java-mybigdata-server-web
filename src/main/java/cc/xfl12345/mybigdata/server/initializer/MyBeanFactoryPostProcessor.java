package cc.xfl12345.mybigdata.server.initializer;


import cc.xfl12345.mybigdata.server.model.StaticSpringApp;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // 设置 alibaba fastjson 为 默认有序
        JSON.DEFAULT_PARSER_FEATURE = Feature.config(
            JSON.DEFAULT_PARSER_FEATURE,
            Feature.OrderedField,
            true
        );
        // 优先初始化一些 Bean
        configurableListableBeanFactory.createBean(StaticSpringApp.class);
    }
}
