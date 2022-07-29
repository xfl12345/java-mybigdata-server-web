package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.model.checker.JsonChecker;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
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
