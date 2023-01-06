package cc.xfl12345.mybigdata.server.web.config;

import cc.xfl12345.mybigdata.server.common.pojo.InstanceGenerator;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public InstanceGenerator<JsonApiResponseData> jsonApiResponseDataInstanceGenerator() {
        return () -> new JsonApiResponseData(ApiConst.VERSION);
    }
}
