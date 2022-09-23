package cc.xfl12345.mybigdata.server.web.config;

import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import cc.xfl12345.mybigdata.server.web.interceptor.ApiRequestInterceptor;
import cc.xfl12345.mybigdata.server.web.interceptor.UploadInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppSpringMvcConfig extends WebMvcAutoConfiguration implements WebMvcConfigurer {
    public AppSpringMvcConfig() {
        super();
    }

    protected UploadInterceptor uploadInterceptor;

    @Autowired
    public void setUploadInterceptor(UploadInterceptor uploadInterceptor) {
        this.uploadInterceptor = uploadInterceptor;
    }

    protected ApiRequestInterceptor apiRequestInterceptor;

    @Autowired
    public void setApiRequestInterceptor(ApiRequestInterceptor apiRequestInterceptor) {
        this.apiRequestInterceptor = apiRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(uploadInterceptor).addPathPatterns("/upload/**");
        registry.addInterceptor(apiRequestInterceptor).addPathPatterns("/" + ApiConst.BACKEND_PATH_NAME + "/**");
    }
}
