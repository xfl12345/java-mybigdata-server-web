package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.model.checker.OfflineJsonChecker;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import net.jimblackler.jsonschemafriend.GenerationException;
import net.jimblackler.jsonschemafriend.SchemaStore;
import net.jimblackler.jsonschemafriend.ValidationException;
import net.jimblackler.jsonschemafriend.Validator;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;

public class TestJsonSchemaValidator extends TestSpringAppLoad {

    @Override
    public void onSpringAppLoaded(ConfigurableApplicationContext applicationContext) {
        String jsonString = "{" +
            "    \"offset\": 666," +
            "    \"limit\": 2333 " +
            "}";

        OfflineJsonChecker jsonChecker = applicationContext.getBean("mybatisRowBoundsObjectChecker", OfflineJsonChecker.class);
        try {
            jsonChecker.check(jsonString);
            System.out.println("true");
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        try {
            jsonChecker = new OfflineJsonChecker(
                applicationContext.getBean("jimFS", FileSystem.class),
                applicationContext.getBean("uuidGenerator", TimeBasedGenerator.class),
                applicationContext.getBean("jsonSchemaStore", SchemaStore.class),
                applicationContext.getBean("jsonValidator", Validator.class),
                IOUtils.toString(Resources.getResourceURL("json/schema/mybatis_row_bounds_object.json"), StandardCharsets.UTF_8),
                applicationContext.getBean("jsonSchemaChecker", OfflineJsonChecker.class)
            );
            jsonChecker.check(jsonString);
            System.out.println("true");
        } catch (ValidationException | GenerationException | IOException e) {
            e.printStackTrace();
        }
    }
}
