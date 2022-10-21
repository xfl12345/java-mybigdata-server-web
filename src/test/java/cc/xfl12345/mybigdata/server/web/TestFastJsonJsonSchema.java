package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.web.model.checker.JsonChecker;
import org.springframework.context.ConfigurableApplicationContext;

public class TestFastJsonJsonSchema extends TestSpringAppLoad {
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
            jsonSchemaChecker.check(baseRequestObjectChecker)
        );
        System.out.print("\n".repeat(10));
    }
}
