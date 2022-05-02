package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.plugin.codemodel.SingleStreamCodeWriter;
import com.sun.codemodel.JCodeModel;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class StudyJsonschema2pojo {
    public static void main(String[] args) throws IOException {
        JCodeModel codeModel = new JCodeModel();

        URL source = Thread.currentThread().getContextClassLoader()
            .getResource("json/schema/mybatis_row_bounds_object.json");

        InputStream inputStream = Objects.requireNonNull(source).openStream();
        String sourceContent = new String(
            inputStream.readAllBytes(),
            StandardCharsets.UTF_8
        );
        inputStream.close();

        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() { // set config option by overriding method
                return false;
            }
        };
//        config.isUseTitleAsClassname();

        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(codeModel, "Schema666", "cc.xfl12345.mybigdata.server.model.database", sourceContent);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SingleStreamCodeWriter codeWriter = new SingleStreamCodeWriter(outputStream);
        codeModel.build(codeWriter);
        codeWriter.close();

        System.out.println(outputStream);
        outputStream.close();

        // TODO 动态生成 class 并加载，直接尝试 跑 bee ORM
    }
}
