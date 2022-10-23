package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import cc.xfl12345.mybigdata.server.web.service.SpringFrameworkService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_BASE + '/' + "app")
public class ApplicationController {
    @Getter
    protected SpringFrameworkService service;

    @Autowired
    public void setService(SpringFrameworkService service) {
        this.service = service;
    }

    @GetMapping("shutdown")
    @ResponseBody
    public boolean shutdown(HttpServletRequest request, boolean confirm) {
        WebApplicationContext context = service.getWebApplicationContext(request);
        if (confirm && context != null) {
            // 三秒后执行退出任务
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
                // 整个 JVM 系统级 关机
                System.exit(0);
            });
            thread.setName("web-api-app-shutdown");
            thread.start();
            return true;
        }

        return false;
    }

    @GetMapping("context/reboot")
    @ResponseBody
    public boolean reboot(HttpServletRequest request, HttpServletResponse response, boolean confirm) {
        return service.reboot(request, response, confirm);
    }
}
