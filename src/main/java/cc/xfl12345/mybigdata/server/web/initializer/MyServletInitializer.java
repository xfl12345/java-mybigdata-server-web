package cc.xfl12345.mybigdata.server.web.initializer;

import cc.xfl12345.mybigdata.server.web.SpringAppOuterHook;
import cc.xfl12345.mybigdata.server.web.SpringAppStatus;
import cc.xfl12345.mybigdata.server.web.MybigdataApplication;
import cc.xfl12345.mybigdata.server.web.appconst.SpringAppLaunchMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Slf4j
public class MyServletInitializer extends SpringBootServletInitializer {
    private static MyServletInitializer instance;

    public static MyServletInitializer getInstance() {
        return instance;
    }

    private ConfigurableApplicationContext springApplicationContext;

    private ServletContext servletContext;

    private ClassLoader mainThreadClassLoader;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.servletContext = servletContext;
        MyServletInitializer.instance = this;
        mainThreadClassLoader = Thread.currentThread().getContextClassLoader();
        SpringAppStatus.launchMode = SpringAppLaunchMode.WAR;
        log.info("我的ServletContext为：" + servletContext.getServletContextName() + "; 初始化时我被调用了。");
        try {
            SpringAppOuterHook.beforeAppStarted();
            super.onStartup(servletContext);
            SpringAppOuterHook.afterAppStarted(springApplicationContext);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MybigdataApplication.class);
    }

    @Override
    protected WebApplicationContext run(SpringApplication application) {
        WebApplicationContext applicationContext = super.run(application);
        springApplicationContext = (ConfigurableApplicationContext) applicationContext;
        return applicationContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public ConfigurableApplicationContext getSpringApplicationContext() {
        return springApplicationContext;
    }

    public ClassLoader getMainThreadClassLoader() {
        return mainThreadClassLoader;
    }
}
