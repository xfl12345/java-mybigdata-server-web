package cc.xfl12345.mybigdata.server.web.controller;

import cc.xfl12345.mybigdata.server.common.data.source.StringTypeSource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @PostConstruct
    public void init() {
        setTmpData(new ConcurrentHashMap<>());
        getTmpData().put("visitCount", 0);
    }

    @Getter
    @Setter
    private Map<String, Object> tmpData;

    @GetMapping(value = "a-cat-a-dog")
    @ResponseBody
    public String aCataDog(String password) {
        synchronized (getTmpData()) {
            Integer visitCount = (Integer) getTmpData().get("visitCount");
            if (visitCount == 0 && password != null && password.equals("hsodfhodsahd45as4da6s4f")) {
                visitCount += 1;
                getTmpData().put("visitCount", visitCount);
                return "vmess://ew0KICAidiI6ICIyIiwNCiAgInBzIjogImNkbi1ub2RlMi1hdXRvLWRucyIsDQogICJhZGQiOiAiYmluLmVudHdhcmUubmV0IiwNCiAgInBvcnQiOiAiNDQzIiwNCiAgImlkIjogIjY1NGRiODJmLTk1OWYtNGE4YS1iMGJkLWQxYzBmN2ZkMzk1MyIsDQogICJhaWQiOiAiMCIsDQogICJzY3kiOiAiYWVzLTEyOC1nY20iLA0KICAibmV0IjogIndzIiwNCiAgInR5cGUiOiAibm9uZSIsDQogICJob3N0IjogImNkbi1ub2RlMi4xMTEwMDAzMzMueHl6IiwNCiAgInBhdGgiOiAiL3lvdXJzcmF5IiwNCiAgInRscyI6ICJ0bHMiLA0KICAic25pIjogIiIsDQogICJhbHBuIjogIiINCn0=";
            } else {
                return "FUCK YOU!";
            }
        }

    }




    @GetMapping(value = "call-test")
    @ResponseBody
    public String callTest() {
        return "Hello,world!";
    }
}
