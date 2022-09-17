package cc.xfl12345.mybigdata.server.web.config;

import org.apache.catalina.core.StandardContext;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {
    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addContextCustomizers(context -> {
            if(context instanceof StandardContext) {
                ((StandardContext)context).setUnloadDelay(5000);
            }
        });

        return tomcat;
    }
}
