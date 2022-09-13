package cc.xfl12345.mybigdata.server.web.controller.restful;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("spring")
public class SpringAppController implements ApplicationContextAware {

    protected ConfigurableApplicationContext applicationContext;

    @Autowired
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @RequestMapping("reload/all")
    @ResponseBody
    public String reloadAll(HttpServletRequest request) {
        WebApplicationContext springMVCContext = RequestContextUtils.findWebApplicationContext(request);
        ApplicationContext parent = null;

        if (springMVCContext == null) {
            parent = applicationContext.getParent();
        } else {
            parent = springMVCContext.getParent();
        }

        if (parent != null) {
            log.info("Root webApplicationContext was found.Refreshing...");
            ((AbstractRefreshableApplicationContext) parent).refresh();
        } else {
            log.info("Root webApplicationContext was not found.");
            applicationContext.refresh();
        }
        return "success";
    }

    @RequestMapping("reload")
    @ResponseBody
    public String reloadSpringContext() {
        applicationContext.refresh();
        return "success";
    }

    @RequestMapping("shutdown")
    @ResponseBody
    public boolean shutdown(HttpServletRequest request, Boolean confirm) {
        if (confirm == null) {
            // TODO 写个页面
        } else {
            if (confirm) {
                // 三秒后执行退出任务
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    SpringApplication.exit(applicationContext);
                });
                thread.start();
                return true;
            }
        }

        return false;
    }
}
