package cc.xfl12345.mybigdata.server.web.config;

import cc.xfl12345.mybigdata.server.web.interceptor.ApiRequestInterceptor;
import cc.xfl12345.mybigdata.server.web.interceptor.UploadInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.AotProxyHint;
import org.springframework.nativex.hint.ProxyBits;

@Configuration(proxyBeanMethods = false)
@AotProxyHint(targetClass = AppSpringMvcInterceptorConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
public class AppSpringMvcInterceptorConfig {
    @Bean
    @ConditionalOnMissingBean
    public UploadInterceptor uploadInterceptor() {
        return new UploadInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiRequestInterceptor apiRequestInterceptor() {
        return new ApiRequestInterceptor();
    }
}
