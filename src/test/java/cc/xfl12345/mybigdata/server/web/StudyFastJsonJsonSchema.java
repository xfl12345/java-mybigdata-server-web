package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.web.model.checker.JsonChecker;
import com.alibaba.fastjson2.schema.JSONSchema;
import org.springframework.context.ConfigurableApplicationContext;

public class StudyFastJsonJsonSchema extends TestSpringAppLoad {
    @Override
    public void onSpringAppLoaded(ConfigurableApplicationContext applicationContext) {

        JsonChecker baseRequestObjectChecker = applicationContext.getBean(
            "baseRequestObjectChecker",
            JsonChecker.class
        );

        JsonChecker jsonSchemaChecker = applicationContext.getBean(
            "jsonSchemaChecker",
            JsonChecker.class
        );


        System.out.print("\n".repeat(10));
        System.out.println(
            JSONSchema.of(jsonSchemaChecker.getJsonObject()).isValid(baseRequestObjectChecker)
        );
        System.out.print("\n".repeat(10));
    }
}
