package cc.xfl12345.mybigdata.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * source code URL=https://blog.csdn.net/fly910905/article/details/87779283
 */
@Controller
@Slf4j
@RequestMapping("reload")
public class ReloadController {
    protected ConfigurableApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ConfigurableApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @RequestMapping("all")
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

    @RequestMapping("spring-context")
    @ResponseBody
    public String reloadSpringContext() {
        applicationContext.refresh();
        return "success";
    }
}
