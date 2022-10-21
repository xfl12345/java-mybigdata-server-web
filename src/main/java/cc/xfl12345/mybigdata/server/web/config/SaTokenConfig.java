package cc.xfl12345.mybigdata.server.web.config;

import cc.xfl12345.mybigdata.server.common.appconst.field.FileOperationField;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.AotProxyHint;
import org.springframework.nativex.hint.ProxyBits;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@AotProxyHint(targetClass = SaTokenConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
public class SaTokenConfig implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> {
            // 根据路由划分模块，不同模块不同鉴权
            SaRouter.match("/upload/**", r -> {
                StpUtil.checkPermission(FileOperationField.UPLOAD);
            });
            SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
            // SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
            // SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
            // SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
            // SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
            // SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
        })).addPathPatterns("/**");
        // .excludePathPatterns("/user/login");
    }
}
