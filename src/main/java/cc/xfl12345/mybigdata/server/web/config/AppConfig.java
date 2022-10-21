package cc.xfl12345.mybigdata.server.web.config;

import cc.xfl12345.mybigdata.server.common.database.error.SqlErrorAnalyst;
import cc.xfl12345.mybigdata.server.web.pojo.WebApiDataErrorHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.AotProxyHint;
import org.springframework.nativex.hint.ProxyBits;

@Configuration(proxyBeanMethods = false)
@AotProxyHint(targetClass = AppConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
public class AppConfig {
    @Bean
    @ConditionalOnMissingBean
    public WebApiDataErrorHandler webApiDataErrorHandler(SqlErrorAnalyst sqlErrorAnalyst) {
        WebApiDataErrorHandler handler = new WebApiDataErrorHandler();
        handler.setSqlErrorAnalyst(sqlErrorAnalyst);

        return handler;
    }
}
