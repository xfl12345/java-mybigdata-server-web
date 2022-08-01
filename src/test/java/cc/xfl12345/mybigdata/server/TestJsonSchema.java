package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.model.checker.JsonChecker;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.context.ConfigurableApplicationContext;

import javax.xml.validation.Schema;

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
            "    \"offset\": 666," +
            "    \"limit\": 2333 " +
            "}";

        JsonChecker mybatisRowBoundsObjectChecker = applicationContext.getBean(
            "mybatisRowBoundsObjectChecker",
            JsonChecker.class
        );


        System.out.println("#".repeat(60));
        System.out.print("\n".repeat(10));
        System.out.println("Test jsonSchemaChecker: " +
            jsonSchemaChecker.getCheckResultAsBoolean(baseRequestObjectChecker.getJsonObject())
        );
        System.out.println("Test mybatisRowBoundsObjectChecker(string): " +
            mybatisRowBoundsObjectChecker.getCheckResultAsBoolean(jsonString)
        );
        System.out.println("Test mybatisRowBoundsObjectChecker(object): " +
            mybatisRowBoundsObjectChecker.getCheckResultAsBoolean(JSONObject.parseObject(jsonString))
        );
        System.out.print("\n".repeat(10));
        System.out.println("#".repeat(60));
    }
}
