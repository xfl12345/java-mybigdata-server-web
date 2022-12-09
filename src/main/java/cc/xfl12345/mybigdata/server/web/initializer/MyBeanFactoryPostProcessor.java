package cc.xfl12345.mybigdata.server.web.initializer;


import cc.xfl12345.mybigdata.server.common.data.source.pojo.CommonMbdId;
import cc.xfl12345.mybigdata.server.common.pojo.MbdId;
import cc.xfl12345.mybigdata.server.web.appconst.EnvConst;
import io.swagger.v3.oas.models.media.StringSchema;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springdoc.core.SpringDocUtils;
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

        // 使 swagger 正确识别自定义类型 （映射自定义类型为其它类型）
        // io.swagger.v3.core.converter.ModelConverters
        SpringDocUtils.getConfig().replaceWithSchema(MbdId.class, new StringSchema());
        SpringDocUtils.getConfig().replaceWithSchema(CommonMbdId.class, new StringSchema());
        SpringDocUtils.getConfig().replaceWithSchema(ObjectId.class, new StringSchema());
    }
}
