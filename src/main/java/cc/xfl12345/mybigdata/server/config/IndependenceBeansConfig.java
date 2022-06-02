package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.CoreTableCache;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.StringTypeHandlerImpl;
import cc.xfl12345.mybigdata.server.model.database.producer.GlobalDataRecordProducer;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.teasoft.honey.osql.core.BeeFactory;

import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

@Configuration
public class IndependenceBeansConfig {
    @Bean("springbootExitCodeGenerator")
    public ExitCodeGenerator getSpringbootExitCodeGenerator() {
        return new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                return 0;
            }
        };
    }

    @Bean(name = "defaultTimeZone")
    public SimpleTimeZone getDefaultTimeZone() {
        return new SimpleTimeZone(28800000, "China Standard Time");
    }

    @Bean(name = "defaultDateFormat")
    public SimpleDateFormat getDefaultDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Bean(name = "millisecondFormatter")
    public SimpleDateFormat getMillisecondFormatter() {
        return new SimpleDateFormat("SSS");
    }

    @Bean(name = "fullDateFormat")
    public SimpleDateFormat getFullDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Bean(name = "uuidGenerator")
    public TimeBasedGenerator getTimeBasedGenerator() {
        return Generators.timeBasedGenerator();
    }
}
