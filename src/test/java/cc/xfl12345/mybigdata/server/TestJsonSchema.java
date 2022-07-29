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

        );
        System.out.print("\n".repeat(10));
    }
}
