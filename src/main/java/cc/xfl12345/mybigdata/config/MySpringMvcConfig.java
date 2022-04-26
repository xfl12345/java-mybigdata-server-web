package cc.xfl12345.mybigdata.config;

import cc.xfl12345.mybigdata.interceptor.ApiRequestInterceptor;
import cc.xfl12345.mybigdata.interceptor.DruidStatInterceptor;
import cc.xfl12345.mybigdata.interceptor.UploadInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.UrlResource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

@Configuration
public class MySpringMvcConfig extends WebMvcAutoConfiguration implements WebMvcConfigurer, ApplicationContextAware {
    protected ApplicationContext applicationContext;

    @Autowired
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public MySpringMvcConfig() {
        super();
    }





    @Bean("uploadInterceptor")
    @Scope("singleton")
    public UploadInterceptor getUploadInterceptor() {
        return new UploadInterceptor();
    }

    @Bean("apiRequestInterceptor")
    @Scope("singleton")
    public ApiRequestInterceptor getApiRequestInterceptor() {
        return new ApiRequestInterceptor();
    }

    @Bean("druidStatInterceptor")
    @Scope("singleton")
    public DruidStatInterceptor getDruidStatInterceptor() {
        return new DruidStatInterceptor();
    }





    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
        try {
            String druidRootPathName = "druid";
            registry.addResourceHandler(
                String.format("/%s/**", druidRootPathName)
            ).addResourceLocations(
                new UrlResource(Thread.currentThread().getContextClassLoader()
                    .getResource("support/http/resources/").toURI())
            );
//            registry.addResourceHandler(
//                String.format("/%s/*.html", druidRootPathName)
//            ).addResourceLocations(
//                new UrlResource(Thread.currentThread().getContextClassLoader()
//                    .getResource("support/http/resources/").toURI())
//            );
//            registry.addResourceHandler(
//                String.format("/%s/js/**", druidRootPathName)
//            ).addResourceLocations(
//                new UrlResource(Thread.currentThread().getContextClassLoader()
//                    .getResource("support/http/resources/js/").toURI())
//            );
//            registry.addResourceHandler(
//                String.format("/%s/css/**", druidRootPathName)
//            ).addResourceLocations(
//                new UrlResource(Thread.currentThread().getContextClassLoader()
//                    .getResource("support/http/resources/css/").toURI())
//            );
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(applicationContext.getBean("uploadInterceptor", HandlerInterceptor.class))
            .addPathPatterns("/upload/**");
        registry.addInterceptor(applicationContext.getBean("apiRequestInterceptor", HandlerInterceptor.class))
            .addPathPatterns("/api/**");
        registry.addInterceptor(applicationContext.getBean("druidStatInterceptor", HandlerInterceptor.class))
            .addPathPatterns("/druid/**");
    }
}
