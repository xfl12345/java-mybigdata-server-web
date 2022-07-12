package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.model.checker.OfflineJsonChecker;
import com.alibaba.fastjson2.JSON;
import org.springframework.context.ConfigurableApplicationContext;

public class StudyJsonSchema extends TestSpringAppLoad {
    @Override
    public void onSpringAppLoaded(ConfigurableApplicationContext applicationContext) {

        OfflineJsonChecker jsonChecker = applicationContext.getBean(
            "baseRequestObjectChecker",
            OfflineJsonChecker.class
        );


        System.out.print("\n".repeat(10));
        System.out.println(
            JSON.toJSON(jsonChecker.getSchema().getSchemaObject()).toString()
        );
        System.out.print("\n".repeat(10));
    }
}
