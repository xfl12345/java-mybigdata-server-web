package cc.xfl12345.mybigdata.server.listener;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileSystemManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;

/**
 * 这是一个用来执行正常退出任务的类
 */
@Component
@WebListener
@Slf4j
public class ContextFinalizer implements ServletContextListener, ApplicationListener<ContextClosedEvent>, ApplicationContextAware, SignalHandler {
    protected ApplicationContext applicationContext;

    public ContextFinalizer() {
        // 注册要监听的信号
        Signal.handle(new Signal("INT"), this);     // 2  : 中断（同 ctrl + c ）
        Signal.handle(new Signal("TERM"), this);    // 15 : 正常终止
    }

    @Autowired
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void contextInitialized(ServletContextEvent sce) {
    }

    public void handle(Signal signal) {
        String signalName = signal.getName();
        log.info(signalName+":"+signal.getNumber());
        if(signalName.equals("INT") || signalName.equals("TERM")) {
            SpringApplication.exit(applicationContext);
        }
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        if (applicationContext.getParent() == null) {
            deregisterJdbcDriver(applicationContext);
            FileSystemManager fileSystemManager = applicationContext.getBean("apacheVfsFileSystemManager", FileSystemManager.class);
            fileSystemManager.close();
        }
    }

    /**
     * 这是一个用来结束JDBC驱动的函数，防止redeploy时被警告存在内存泄露风险
     */
    public static void deregisterJdbcDriver(ApplicationContext springAppContext) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        TreeSet<String> dataSourceBeanNames = new TreeSet<>();
        if (drivers.hasMoreElements()) {
            if (springAppContext != null) {
                new TreeSet<>(List.of(springAppContext.getBeanNamesForType(DataSource.class)));
            }
            Driver d = null;
            String driverInstanceName;
            while (drivers.hasMoreElements()) {
                try {
                    d = drivers.nextElement();
                    driverInstanceName = d.toString();
                    DriverManager.deregisterDriver(d);
                    log.info(String.format("Driver %s deregistered", driverInstanceName));
                } catch (SQLException ex) {
                    log.error(String.format("Error deregistering driver %s", d) + ":" + ex);
                }
                //像队列一样遍历列表。循环到队列为空的时候才退出
                if (!drivers.hasMoreElements()) {
                    // 如果出现了不认识的 bean ，将会导致 bean 数量无法清零。
                    // 为了防止陷入死循环，允许直接使用常规（暴力）手段卸载
                    drivers = DriverManager.getDrivers();
                }
            }
            log.info("JDBC driver clean.");
        }
    }
}
