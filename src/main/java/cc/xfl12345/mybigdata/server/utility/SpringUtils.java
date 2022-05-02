package cc.xfl12345.mybigdata.server.utility;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

public class SpringUtils {

    @SuppressWarnings("unchecked")
    public static <T> T getAutoWireBean(ApplicationContext applicationContext, Class<T> cls) {
        return (T) applicationContext.getAutowireCapableBeanFactory()
            .autowire(cls, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
    }
}
