package cc.xfl12345.mybigdata.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Slf4j
public class MyWebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("我的ServletContext为：" + servletContext.getServletContextName() + "; 初始化时我被调用了。");
    }
}
