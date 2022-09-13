package cc.xfl12345.mybigdata.server.web.initializer;

import cc.xfl12345.mybigdata.server.web.MybigdataApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MybigdataApplication.class);
    }

}
