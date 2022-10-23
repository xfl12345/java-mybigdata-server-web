package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.web.appconst.SpringAppLaunchMode;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.nativex.hint.NativeHint;

import static cc.xfl12345.mybigdata.server.web.SpringAppStatus.restartCount;

@NativeHint(options = "--initialize-at-build-time=org.apache.commons.logging.impl.SLF4JLocationAwareLog")
// @AotProxyHint(targetClass = cc.xfl12345.mybigdata.server.web.config.AppConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
// @AotProxyHint(targetClass = cc.xfl12345.mybigdata.server.web.config.AppSpringMvcConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
// @AotProxyHint(targetClass = cc.xfl12345.mybigdata.server.web.config.AppSpringMvcInterceptorConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
// @AotProxyHint(targetClass = cc.xfl12345.mybigdata.server.web.config.IndependenceBeansConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
// @AotProxyHint(targetClass = cc.xfl12345.mybigdata.server.web.config.JSONSchemaConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
// @AotProxyHint(targetClass = cc.xfl12345.mybigdata.server.web.config.SaTokenConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
// @AotProxyHint(targetClass = cc.xfl12345.mybigdata.server.web.config.TomcatConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
// @AotProxyHint(targetClass = cc.xfl12345.mybigdata.server.web.config.UiResourceConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
// @AotProxyHint(targetClass = cc.xfl12345.mybigdata.server.web.config.VFSConfig.class, proxyFeatures = ProxyBits.IS_STATIC)
@EnableConfigurationProperties
@SpringBootApplication
public class MybigdataApplication {
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) throws Exception {
        SpringAppStatus.launchMode = SpringAppLaunchMode.JAR;
        try {
            SpringAppOuterHook.beforeAppStarted();
            context = SpringApplication.run(MybigdataApplication.class, args);
            SpringAppOuterHook.afterAppStarted(context);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }  catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(MybigdataApplication.class, args.getSourceArgs());
        });

        restartCount += 1;
        thread.setDaemon(false);
        thread.setName("restart-" + restartCount);
        thread.start();
    }
}
