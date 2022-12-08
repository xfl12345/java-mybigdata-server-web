package cc.xfl12345.mybigdata.server.web.config;

import cc.xfl12345.mybigdata.server.common.api.InstanceGenerator;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
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
    public InstanceGenerator<JsonApiResponseData> jsonApiResponseDataInstanceGenerator() {
        return () -> new JsonApiResponseData(ApiConst.VERSION);
    }
}
