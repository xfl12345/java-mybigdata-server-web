package cc.xfl12345.mybigdata.server.web.config;

import org.apache.catalina.core.StandardContext;
import org.apache.coyote.AbstractProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.AotProxyHint;
import org.springframework.nativex.hint.ProxyBits;

@Configuration(proxyBeanMethods = false)
@AotProxyHint(targetClass = TomcatConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
public class TomcatConfig {
    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addContextCustomizers(context -> {
            if(context instanceof StandardContext standardContext) {
                standardContext.setUnloadDelay(5000);
            }
        });
        // 连接超时设置为 20秒
        tomcat.addConnectorCustomizers(connector -> {
            if (connector.getProtocolHandler() instanceof AbstractProtocol<?> abstractProtocol) {
                abstractProtocol.setConnectionTimeout(20000);
            }
        });

        return tomcat;
    }
}
