package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.model.StaticSpringApp;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
public abstract class TestSpringAppLoadBase {

    @Test
    void contextLoads() {
        onSpringAppLoaded(StaticSpringApp.springAppContext);
    }

    public void onSpringAppLoaded(ConfigurableApplicationContext applicationContext) {
    }
}
