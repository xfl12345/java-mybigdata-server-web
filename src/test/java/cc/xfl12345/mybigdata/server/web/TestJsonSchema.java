package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.web.model.checker.JsonChecker;
import org.springframework.context.ConfigurableApplicationContext;

public class TestJsonSchema extends TestSpringAppLoad {
    @Override
    public void onSpringAppLoaded(ConfigurableApplicationContext applicationContext) {
        JsonChecker jsonSchemaChecker = applicationContext.getBean(
            "jsonSchemaChecker",
            JsonChecker.class
        );

        JsonChecker baseRequestObjectChecker = applicationContext.getBean(
            "baseRequestObjectChecker",
            JsonChecker.class
        );

        String jsonString = "{" +
            "    \"operation\": \"hello\"," +
            "    \"data\": {\"msg\": \"2333\"} " +
            "}";

        System.out.println("#".repeat(60));
        System.out.print("\n".repeat(10));
        System.out.println("Test jsonSchemaChecker: " +
            jsonSchemaChecker.check(baseRequestObjectChecker.getJsonObject())
        );
        System.out.println("Test baseRequestObjectChecker(string): " +
            baseRequestObjectChecker.check(jsonString)
        );
        System.out.print("\n".repeat(10));
        System.out.println("#".repeat(60));
    }
}
