package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.interceptor.ApiRequestInterceptor;
import cc.xfl12345.mybigdata.server.interceptor.MySaRouteInterceptor;
import cc.xfl12345.mybigdata.server.interceptor.UploadInterceptor;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MySpringMvcConfig extends WebMvcAutoConfiguration implements WebMvcConfigurer, ApplicationContextAware {
    protected ApplicationContext applicationContext;

    @Autowired
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public MySpringMvcConfig() {
        super();
    }


    @Bean
    public UploadInterceptor uploadInterceptor() {
        return new UploadInterceptor();
    }

    @Bean
    public ApiRequestInterceptor apiRequestInterceptor() {
        return new ApiRequestInterceptor();
    }




    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token的路由拦截器
        registry.addInterceptor(new MySaRouteInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(applicationContext.getBean("uploadInterceptor", HandlerInterceptor.class))
            .addPathPatterns("/upload/**");
        registry.addInterceptor(applicationContext.getBean("apiRequestInterceptor", HandlerInterceptor.class))
            .addPathPatterns("/api/**");
    }
}
