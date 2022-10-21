package cc.xfl12345.mybigdata.server.web.controller;

import cc.xfl12345.mybigdata.server.common.data.source.StringTypeSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping(value = "debug")
public class DebugController {
    @GetMapping(value = "")
    public String debugView(HttpServletRequest request) {
        try {
            ApplicationContext applicationContext = RequestContextUtils.findWebApplicationContext(request);
            if (applicationContext != null) {
                StringTypeSource stringTypeSource = applicationContext.getBean(
                    StringTypeSource.class
                );
                log.debug(stringTypeSource.selectId("text").toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return "debug";
    }

    @GetMapping(value = "call-test")
    @ResponseBody
    public String callTest() {
        return "Hello,world!";
    }
}
