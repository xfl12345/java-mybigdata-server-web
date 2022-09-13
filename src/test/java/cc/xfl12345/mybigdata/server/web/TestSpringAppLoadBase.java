package cc.xfl12345.mybigdata.server.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
public abstract class TestSpringAppLoadBase {

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Test
    void contextLoads() {
        onSpringAppLoaded(applicationContext);
    }

    public void onSpringAppLoaded(ConfigurableApplicationContext applicationContext) {
    }
}
