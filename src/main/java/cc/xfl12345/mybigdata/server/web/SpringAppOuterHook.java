package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.common.utility.ConsoleCharsetUtils;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class SpringAppOuterHook {
    protected static Map<Class<?>, Object> singletons = new HashMap<>();

    public static void beforeAppStarted() throws Exception {
        ConsoleCharsetUtils consoleCharsetUtils = new ConsoleCharsetUtils();
        consoleCharsetUtils.init();
        singletons.put(ConsoleCharsetUtils.class, consoleCharsetUtils);
    }

    public static void afterAppStarted(ApplicationContext applicationContext) throws Exception {
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSingletonByClass(Class<T> cls) {
        Object obj = singletons.get(cls);
        return obj == null ? null : (T) obj;
    }
}
