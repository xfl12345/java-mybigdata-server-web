package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.common.utility.ConsoleCharsetUtils;
import cc.xfl12345.mybigdata.server.web.appconst.EnvConst;
import org.fusesource.jansi.AnsiConsole;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpringAppOuterHook {
    protected static Map<Class<?>, Object> singletons = new HashMap<>();

    public static void beforeAppStarted() throws Exception {
        String springOutputAnsiEnabled = System.getProperty(EnvConst.SPRING_OUTPUT_ANSI_ENABLED);
        // 如果是 ALWAYS 选项，不管终端是否支持 ANSI转义 ，统统输出
        if (springOutputAnsiEnabled != null && !"".equals(springOutputAnsiEnabled) &&
            AnsiOutput.Enabled.ALWAYS.name().toUpperCase(Locale.ROOT).equals(springOutputAnsiEnabled.toUpperCase(Locale.ROOT))) {
            System.setProperty(AnsiConsole.JANSI_MODE, AnsiConsole.JANSI_MODE_FORCE);
        }

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
