package cc.xfl12345.mybigdata.server.web.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;

public class ContextStartedEventListener implements ApplicationListener<ContextStartedEvent> {
    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        AllEventListener.applicationContext = event.getApplicationContext();
    }
}
