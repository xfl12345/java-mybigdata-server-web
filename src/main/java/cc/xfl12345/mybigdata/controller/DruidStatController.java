package cc.xfl12345.mybigdata.controller;

import com.alibaba.druid.stat.DruidStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("druid")
public class DruidStatController implements ApplicationContextAware {
    protected DruidStatService statService = DruidStatService.getInstance();
    protected ApplicationContext applicationContext;

    @Autowired
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @RequestMapping("{partOne:\\w+\\.json$}")
    public @ResponseBody String any(@PathVariable String partOne) {
        return statService.service("/" + partOne);
    }
}
