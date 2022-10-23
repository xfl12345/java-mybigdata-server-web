package cc.xfl12345.mybigdata.server.web.service;

import cc.xfl12345.mybigdata.server.web.MybigdataApplication;
import cc.xfl12345.mybigdata.server.web.SpringAppStatus;
import cc.xfl12345.mybigdata.server.web.appconst.SpringAppLaunchMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service("springFrameworkService")
public class SpringFrameworkService {
    public WebApplicationContext getWebApplicationContext(HttpServletRequest request) {
        return RequestContextUtils.findWebApplicationContext(request);
    }

    public boolean shutdown(ApplicationContext context, boolean confirm) {
        if (confirm && context != null) {
            // 三秒后执行退出任务
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
                SpringApplication.exit(context);
            });
            thread.setName("web-api-spring-shutdown");
            thread.start();
            return true;
        }

        return false;
    }

    public boolean reboot(HttpServletRequest request, HttpServletResponse response, boolean confirm) {
        WebApplicationContext context = getWebApplicationContext(request);
        if (confirm && context != null) {
            if (SpringAppStatus.launchMode == SpringAppLaunchMode.JAR) {
                // 三秒后执行退出任务
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage());
                    }
                    MybigdataApplication.restart();
                });
                thread.setName("web-api-spring-reboot");
                thread.start();
                return true;
            }

            response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }

        return false;
    }
}
