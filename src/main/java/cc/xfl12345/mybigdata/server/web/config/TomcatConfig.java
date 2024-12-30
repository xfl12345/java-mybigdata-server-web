package cc.xfl12345.mybigdata.server.web.config;

import org.apache.catalina.core.StandardContext;
import org.apache.coyote.AbstractProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        // 预留 50 秒给 StandardContext 正常关闭
        return context -> {
            if (context instanceof StandardContext standardContext) {
                standardContext.setUnloadDelay(5000);
            }
        };
    }

    @Bean
    public TomcatConnectorCustomizer tomcatConnectorCustomizer() {
        // 连接超时设置为 20秒
        return connector -> {
            if (connector.getProtocolHandler() instanceof AbstractProtocol<?> abstractProtocol) {
                abstractProtocol.setConnectionTimeout(20000);
            }
        };
    }
}
