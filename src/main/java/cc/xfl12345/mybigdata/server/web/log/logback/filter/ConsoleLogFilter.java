package cc.xfl12345.mybigdata.server.web.log.logback.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConsoleLogFilter extends Filter<ILoggingEvent> {
    protected final static String druidSql = "druid.sql";
    protected final static String druid = "com.alibaba.druid";

    protected HashMap<String, Level> fixedThreshold;

    protected ConcurrentHashMap<String, String> secondLevelCache;

    public ConsoleLogFilter() {
        fixedThreshold = new HashMap<>(13);
        fixedThreshold.put("org.apache.coyote.http11", Level.INFO);
        fixedThreshold.put(druidSql, Level.INFO);
        fixedThreshold.put(druid, Level.INFO);
        fixedThreshold.put("java", Level.INFO);
        fixedThreshold.put("javax", Level.INFO);
        fixedThreshold.put("sun.rmi", Level.INFO);
        fixedThreshold.put("org.apache.tomcat", Level.INFO);
        fixedThreshold.put("org.apache.catalina", Level.INFO);
        fixedThreshold.put("org.apache.catalina.util.LifecycleBase", Level.DEBUG);
        fixedThreshold.put("org.apache.catalina.mapper.MapperListener", Level.DEBUG);
        fixedThreshold.put("org.springframework.validation.beanvalidation", Level.INFO);
        fixedThreshold.put("org.aspectj", Level.INFO);
        fixedThreshold.put("com.networknt.schema", Level.INFO);

        secondLevelCache = new ConcurrentHashMap<>(fixedThreshold.size() << 2);
    }


    @Override
    public FilterReply decide(ILoggingEvent event) {
        String loggerName = event.getLoggerName();
        Level level = event.getLevel();

        // 看看有没有缓存
        String prefix = secondLevelCache.get(loggerName);
        if (prefix == null) {
            for (String loggerPrefix : fixedThreshold.keySet()) {
                if (isMatch(loggerPrefix, loggerName)) {
                    // 缓存起来
                    secondLevelCache.put(loggerName, loggerPrefix);
                    if (!level.isGreaterOrEqual(fixedThreshold.get(loggerPrefix))) {
                        return FilterReply.DENY;
                    }

                    break;
                }
            }
        } else {
            if (!level.isGreaterOrEqual(fixedThreshold.get(prefix))) {
                return FilterReply.DENY;
            }
        }

        return FilterReply.NEUTRAL;
    }

    protected boolean isMatch(String prefix, String name) {
        // 被检字符串 都没有 前缀 长，直接淘汰
        if (name == null || name.length() < prefix.length()) {
            return false;
        }

        int prefixLength = prefix.length();
        if (prefixLength <= 6) {
            for (int i = 0; i < prefixLength; i++) {
                if (prefix.charAt(i) != name.charAt(i)) {
                    return false;
                }
            }

            return true;
        }

        boolean result = true;
        // 判断总长度是否是奇数，以减少一次比较。
        boolean odd = (prefixLength & 0x1) == 0x1;
        int end = prefixLength - 1;
        int mid = prefixLength / 2;
        // 两头向中间靠拢，一定程度上提高碰撞概率
        for (int i = 0, currentEnd = end; i < mid; i++, currentEnd--) {
            if (prefix.charAt(i) != name.charAt(i)) {
                result = false;
                break;
            }

            if (prefix.charAt(currentEnd) != name.charAt(currentEnd)) {
                result = false;
                break;
            }
        }

        if (result && odd) {
            result = prefix.charAt(mid) == name.charAt(mid);
        }

        return result;
    }
}
