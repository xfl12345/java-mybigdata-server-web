package cc.xfl12345.mybigdata.server;

import org.springframework.context.ConfigurableApplicationContext;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.honey.osql.autogen.GenBean;
import org.teasoft.honey.osql.autogen.GenConfig;
import org.teasoft.honey.osql.core.HoneyConfig;

import java.nio.charset.StandardCharsets;

public class StudyBeeOrmCodeGenerator extends TestSpringAppLoad {

    @Override
    public void onSpringAppLoaded(ConfigurableApplicationContext applicationContext) {
        try {
            String dbName = HoneyConfig.getHoneyConfig().getDbName();
            //driverName,url,username,password config in bee.properties.

            GenConfig config = new GenConfig();
            config.setDbName(dbName);
            config.setGenToString(true);//生成toString方法
            config.setGenSerializable(true);
            config.setEncode(StandardCharsets.UTF_8.name());

            //更改成本地的具体路径  change to your real path
            //config.setBaseDir("D:\\xxx\\yyy\\bee-exam\\src\\main\\java\\");
            config.setBaseDir("tmp666");
            //config.setPackagePath("org.teasoft.exam.bee.osql.entity");
            config.setPackagePath("cc.xfl12345.mybigdata.server");

            GenBean genBean = new GenBean(config);
            config.setCommentPlace(1);

            //设置相对Entity的文件夹; 空表示与Entity同一个文件夹
            //config.setFieldFileRelativeFolder("field");

            genBean.genSomeBeanFile("global_data_record");

        } catch (BeeException e) {
            e.printStackTrace();
        }
        super.onSpringAppLoaded(applicationContext);
    }
}
