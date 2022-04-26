package cc.xfl12345.mybigdata.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Slf4j
public class MySpringAppRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    public static WebApplicationContext webApplicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        if (applicationContext.getParent() != null) {
            webApplicationContext = (WebApplicationContext) applicationContext;
        }
        log.info("我的父容器为：" + applicationContext.getParent() + "; 初始化完成之后，我被调用了。");
    }
}



