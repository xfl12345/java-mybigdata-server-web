package cc.xfl12345.mybigdata.server.web.initializer;

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

    private ConfigurableApplicationContext context;

    private ServletContext servletContext;

    private ClassLoader mainThreadClassLoader;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        mainThreadClassLoader = Thread.currentThread().getContextClassLoader();
        this.servletContext = servletContext;
        log.info("我的ServletContext为：" + servletContext.getServletContextName() + "; 初始化时我被调用了。");
        super.onStartup(servletContext);
        MyServletInitializer.instance = this;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        SpringAppStatus.launchMode = SpringAppLaunchMode.WAR;
        return application.sources(MybigdataApplication.class);
    }

    @Override
    protected WebApplicationContext run(SpringApplication application) {
        WebApplicationContext applicationContext = super.run(application);
        context = (ConfigurableApplicationContext) applicationContext;
        return applicationContext;
    }

    public void restart() {
        Thread thread = new Thread(() -> {
            SpringApplication.exit(context);
            try {
                onStartup(servletContext);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
            // context.refresh();
        });

        thread.setDaemon(false);
        thread.setContextClassLoader(mainThreadClassLoader);
        thread.start();
    }
}
