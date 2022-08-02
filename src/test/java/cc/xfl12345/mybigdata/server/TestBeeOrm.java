package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.springframework.context.ConfigurableApplicationContext;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestBeeOrm extends TestSpringAppLoad {
    @Override
    public void onSpringAppLoaded(ConfigurableApplicationContext applicationContext) {
        TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

        SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();

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

        String wait2insert = "0123456789".repeat(100);
        StringContent stringContent = new StringContent();
        stringContent.setContent(wait2insert);
        stringContent.setGlobalId(recordList.get(0).getId());
        int rowCount = 0;
        try {
            rowCount = suid.insert(stringContent);
        } catch (BeeException e) {
            System.out.println(e.getMessage());
            Throwable cause = e.getCause();
            // 如果是 MySQL 报错
            if (cause instanceof SQLException sqlException) {
                int errorCode = sqlException.getErrorCode();
                switch (errorCode) {
                    case 1059: //ER_TOO_LONG_IDENT -- Identifier name '%s' is too long
                        break;
                    case 1060: //ER_DUP_FIELDNAME -- Duplicate column name '%s' -- 字段内容重复（unique限制）
                        break;
                    case 1061: //ER_DUP_KEYNAME -- Duplicate key name '%s'
                        break;
                    case 1062: //ER_DUP_ENTRY -- Duplicate entry '%s' for key %d
                        break;
                    case 1406: // ER_DATA_TOO_LONG -- Data too long for column '%s' at row %ld -- 数据超出字段长度
                        break;

                }
            }
        }

        System.out.println(rowCount);

    }
}
