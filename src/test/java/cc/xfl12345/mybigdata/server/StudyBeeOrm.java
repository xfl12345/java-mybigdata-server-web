package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;
import org.teasoft.honey.osql.core.HoneyFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudyBeeOrm {
    public static void main(String[] args) throws IOException, SQLException {
        Yaml yaml = new Yaml();
        URL fileURL = ClassLoader.getSystemClassLoader().getResource("application.yml");
        ClassLoader.getSystemClassLoader().getResource("org/springframework/boot/logging/logback/defaults.xml");
        InputStream inputStream = Objects.requireNonNull(fileURL).openStream();
        JSONObject jsonObject = yaml.loadAs(inputStream, JSONObject.class);
        inputStream.close();
        System.out.println(jsonObject.toString(SerializerFeature.PrettyFormat));
        JSONObject datasourceConfig = jsonObject
            .getJSONObject("spring")
            .getJSONObject("datasource");
        String dbLoginUserName = datasourceConfig.getString("username");
        String dbLoginPassword = datasourceConfig.getString("password");
        String jdbcURLinString = datasourceConfig.getString("url");
        String driverClassName = datasourceConfig.getString("driver-class-name");

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(dbLoginUserName);
        dataSource.setPassword(dbLoginPassword);
        dataSource.setUrl(jdbcURLinString);
        dataSource.setDriverClassName(driverClassName);
        dataSource.init();

        BeeFactory beeFactory = BeeFactory.getInstance();
        beeFactory.setDataSource(dataSource);

        StudyBeeOrm studyBeeOrm = new StudyBeeOrm();
        studyBeeOrm.justTest(BeeFactory.getHoneyFactory());
    }

    public void justTest(HoneyFactory honeyFactory) {
        TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

        SuidRich suid = honeyFactory.getSuidRich();

        ArrayList<GlobalDataRecord> records = new ArrayList<>();
        GlobalDataRecord globalDataRecord = new GlobalDataRecord();
        globalDataRecord.setUuid(uuidGenerator.generate().toString());
        records.add(globalDataRecord);
        globalDataRecord = new GlobalDataRecord();
        globalDataRecord.setUuid(uuidGenerator.generate().toString());
        records.add(globalDataRecord);
        System.out.println(suid.insert(records));

        Condition condition = new ConditionImpl();
//        Condition condition = BeeFactoryHelper.getCondition();
        condition.op("table_name", Op.eq, null);
        List<GlobalDataRecord> recordList = suid.select(new GlobalDataRecord(), condition);

        System.out.println(recordList);

        // 超字段长度测试
        String wait2insert = "0123456789".repeat(100);
        StringContent stringContent = new StringContent();
        stringContent.setContent(wait2insert);
        stringContent.setGlobalId(recordList.get(0).getId());
        executeInsert(suid, stringContent);

        // unique key 冲突测试
        wait2insert = "text";
        stringContent = new StringContent();
        stringContent.setContent(wait2insert);
        stringContent.setGlobalId(recordList.get(0).getId());
        executeInsert(suid, stringContent);

        // 主键重复测试
        globalDataRecord = new GlobalDataRecord();
        globalDataRecord.setUuid(uuidGenerator.generate().toString());
        globalDataRecord.setId(recordList.get(0).getId());
        executeInsert(suid, globalDataRecord);
    }

    public int executeInsert(SuidRich suid, Object obj) {
        int rowCount = 0;
        try {
            rowCount = suid.insert(obj);
        } catch (BeeException e) {
            System.out.println(e.getMessage());
            Throwable cause = e.getCause();
            // 如果是 MySQL 报错
            if (cause instanceof SQLException sqlException) {
                int errorCode = sqlException.getErrorCode();
                switch (errorCode) {
                    case 1059: //ER_TOO_LONG_IDENT -- Identifier name '%s' is too long
                        System.out.println("键名太长");
                        break;
                    case 1060: //ER_DUP_FIELDNAME -- Duplicate column name '%s' -- 字段内容重复（unique限制）
                        System.out.println("字段名称重复）");
                        break;
                    case 1061: //ER_DUP_KEYNAME -- Duplicate key name '%s'
                        System.out.println("键名重复");
                        break;
                    case 1062: //ER_DUP_ENTRY -- Duplicate entry '%s' for key %d
                        System.out.println("字段内容重复（unique限制）");
                        break;
                    case 1406: // ER_DATA_TOO_LONG -- Data too long for column '%s' at row %ld -- 数据超出字段长度
                        System.out.println("数据超出字段长度");
                        break;
                    default:
                        break;
                }
            }
        }

        System.out.println(rowCount);
        return rowCount;
    }
}
