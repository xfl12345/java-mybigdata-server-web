package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.web.MybigdataApplication;
import cc.xfl12345.mybigdata.server.web.SpringAppStatus;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import cc.xfl12345.mybigdata.server.web.appconst.SpringAppLaunchMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_BASE + '/' + "spring")
public class SpringFrameworkController {
    @GetMapping("shutdown")
    @ResponseBody
    public boolean shutdown(HttpServletRequest request, boolean confirm) {
        if (confirm) {
            WebApplicationContext context = RequestContextUtils.findWebApplicationContext(request);
            if (context == null) {
                return false;
            }

            // 三秒后执行退出任务
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
                SpringApplication.exit(context);
            });
            thread.start();
            return true;
        }

        return false;
    }

    @GetMapping("reboot")
    @ResponseBody
    public boolean reboot(HttpServletRequest request, HttpServletResponse response, boolean confirm) {
        WebApplicationContext context = RequestContextUtils.findWebApplicationContext(request);
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
                thread.start();
                return true;
            }

            response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }

        return false;
    }
}
