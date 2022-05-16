package cc.xfl12345.mybigdata.server.controller;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping(DruidStatController.servletName)
public class DruidStatController implements ApplicationContextAware {
    protected ApplicationContext applicationContext;

    @Autowired
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected DruidStatService statService = DruidStatService.getInstance();
    public static final String servletName = "druid";
    public static final String servletPathCache1 = "/" + servletName;
    public static final String servletPathCache2 = "/" + servletName + "/";
    public static final String servletIndexPagePathCache = servletName + "/index.html";

    @RequestMapping(path = {"", "index"})
    public void indexView(HttpServletResponse response) throws IOException {
        response.sendRedirect(servletIndexPagePathCache);
    }

    @RequestMapping("{partOne:^\\w+.+}.json")
    public @ResponseBody String root(HttpServletRequest request, @PathVariable String partOne) {
        String relativeURL = request.getServletPath().substring(servletPathCache1.length());
        String httpGetQueryString = request.getQueryString();
        if(httpGetQueryString != null && !httpGetQueryString.isEmpty()) {
            relativeURL += '?' + httpGetQueryString;
        }
        return statService.service(relativeURL);
    }

    @RequestMapping(value = {"weburi-/{partOne:.*}.json", "weburi-/*/{partOne:.*}.json", "weburi-/*/*/{partOne:.*}.json"})
    public @ResponseBody String weburi(HttpServletRequest request, @PathVariable String partOne) {
        return root(request, partOne);
    }
}
