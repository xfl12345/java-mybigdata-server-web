package cc.xfl12345.mybigdata.server;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.TemplateType;

import java.io.IOException;
import java.sql.SQLException;


public class TestMybatisPlusGenerator {

    // 演示例子
    public static void main(String[] args) throws SQLException, IOException {
        // 代码生成器
        FastAutoGenerator autoGenerator = FastAutoGenerator.create(
            new DataSourceConfig.Builder(TestLoadDataSource.getDataSource())
        );

        String projectPath = System.getProperty("user.dir");
        autoGenerator
            // 全局配置
            .globalConfig(builder -> {
                builder.author("xfl12345")
                    .outputDir(projectPath + "/src/main/java");
            })
            .packageConfig(builder -> {
                builder.parent("com.example.generate");
            })
            .templateConfig(builder -> {
                builder
                    .disable(TemplateType.CONTROLLER)
                    .disable(TemplateType.SERVICE)
                    .disable(TemplateType.MAPPER)
                    .disable(TemplateType.SERVICEIMPL)
                    .disable(TemplateType.XML)
                    .entity("/mybatis/MyEntity.java");
            });
        autoGenerator.execute();
    }
}
