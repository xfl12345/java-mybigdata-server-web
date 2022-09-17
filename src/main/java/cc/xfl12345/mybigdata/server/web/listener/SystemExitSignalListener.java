package cc.xfl12345.mybigdata.server.web.listener;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * 这是一个用来监听系统终结信号执行正常退出任务的类
 */
@Component
@Slf4j
public class SystemExitSignalListener implements ApplicationContextAware, SignalHandler {
    protected ApplicationContext applicationContext;

    public SystemExitSignalListener() {
        // 注册要监听的信号
        Signal.handle(new Signal("INT"), this);     // 2  : 中断（同 ctrl + c ）
        Signal.handle(new Signal("TERM"), this);    // 15 : 正常终止
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void handle(Signal signal) {
        String signalName = signal.getName();
        log.info(signalName+":"+signal.getNumber());
        if(signalName.equals("INT") || signalName.equals("TERM")) {
            SpringApplication.exit(applicationContext);
        }
    }
}
