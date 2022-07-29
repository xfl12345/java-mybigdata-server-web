package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.model.checker.JsonChecker;
import net.jimblackler.jsonschemafriend.ValidationException;
import org.springframework.context.ConfigurableApplicationContext;

public class TestJsonSchemaValidator extends TestSpringAppLoad {

    @Override
    public void onSpringAppLoaded(ConfigurableApplicationContext applicationContext) {
        String jsonString = "{" +
            "    \"offset\": 666," +
            "    \"limit\": 2333 " +
            "}";

        JsonChecker jsonChecker = applicationContext
            .getBean("mybatisRowBoundsObjectChecker", JsonChecker.class);

        try {
            System.out.println("#".repeat(60));
            jsonChecker.check(jsonString);
            System.out.println("true"); // 如果没有报错，意味着格式正确
            System.out.println("#".repeat(60));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
