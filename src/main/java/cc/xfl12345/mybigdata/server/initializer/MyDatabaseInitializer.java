package cc.xfl12345.mybigdata.server.initializer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("myDatabaseInitializer")
@Slf4j
public class MyDatabaseInitializer implements InitializingBean {



    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
