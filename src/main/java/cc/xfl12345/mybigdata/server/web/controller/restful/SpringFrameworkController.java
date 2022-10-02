package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_BASE + '/' + "spring")
public class SpringFrameworkController {
    @GetMapping("reload/all")
    @ResponseBody
    public String reloadAll(HttpServletRequest request, boolean confirm) {
        if (!confirm) {
            return "cancelled";
        }

        WebApplicationContext springMVCContext = RequestContextUtils.findWebApplicationContext(request);
        ApplicationContext parent = null;

        if (springMVCContext != null) {
            parent = springMVCContext.getParent();

            if (parent != null) {
                log.info("Root webApplicationContext was found.Refreshing...");
                ((AbstractRefreshableApplicationContext) parent).refresh();
            } else {
                log.info("Root webApplicationContext was not found.");
                ((ConfigurableApplicationContext) springMVCContext).refresh();
            }

            return "succeed";
        }

        return "failed";
    }

    @GetMapping("reload")
    @ResponseBody
    public String reloadSpringContext(HttpServletRequest request, boolean confirm) {
        if (!confirm) {
            return "cancelled";
        }

        WebApplicationContext springMVCContext = RequestContextUtils.findWebApplicationContext(request);
        if (springMVCContext == null) {
            return "failed";
        } else {
            ((ConfigurableApplicationContext) springMVCContext).refresh();
            return "succeed";
        }
    }

    @GetMapping("shutdown")
    @ResponseBody
    public boolean shutdown(HttpServletRequest request, boolean confirm) {
        if (confirm) {
            WebApplicationContext springMVCContext = RequestContextUtils.findWebApplicationContext(request);
            if (springMVCContext == null) {
                return false;
            }

            // 三秒后执行退出任务
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
                SpringApplication.exit(springMVCContext);
            });
            thread.start();
            return true;
        }

        return false;
    }
}
