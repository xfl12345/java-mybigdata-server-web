package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.model.database.association.StringContentAssociation;
import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.teasoft.bee.osql.*;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;
import org.teasoft.honey.osql.core.HoneyFactory;
import org.teasoft.honey.osql.core.ObjectToSQLRich;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudyBeeOrm {
    public static void main(String[] args) throws IOException, SQLException {
        DruidDataSource dataSource = TestLoadDataSource.getDataSource();

        BeeFactory beeFactory = BeeFactory.getInstance();
        beeFactory.setDataSource(dataSource);

        StudyBeeOrm studyBeeOrm = new StudyBeeOrm();
        studyBeeOrm.justTest(BeeFactory.getHoneyFactory());
    }

    public void justTest(HoneyFactory honeyFactory) {
        TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

        SuidRich suid = honeyFactory.getSuidRich();
        Class<?> originStringContentClass = cc.xfl12345.mybigdata.server.model.database.table.StringContent.class;

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
        executeInsert(honeyFactory, stringContent);

        // unique key 冲突测试
        wait2insert = "text";
        stringContent = new StringContent();
        stringContent.setContent(wait2insert);
        stringContent.setGlobalId(recordList.get(0).getId());
        executeInsert(honeyFactory, stringContent);

        // 主键重复测试
        globalDataRecord = new GlobalDataRecord();
        globalDataRecord.setUuid(uuidGenerator.generate().toString());
        globalDataRecord.setId(recordList.get(0).getId());
        executeInsert(honeyFactory, globalDataRecord);


        StringContentAssociation associationQuery = new StringContentAssociation();
        associationQuery.setContent("text");
        MoreTable moreTable = honeyFactory.getMoreTable();
        System.out.println(JSON.toJSONString(moreTable.select(associationQuery)));

    }

    public int executeInsert(HoneyFactory honeyFactory, Object obj) {
        // 尝试插入数据
        int rowCount = 0;
        try {
            rowCount = honeyFactory.getSuidRich().insert(obj);
        } catch (BeeException e) {
            System.out.println(e.getMessage());
            Throwable cause = e.getCause();
            if (cause instanceof SQLException sqlException) {
                int errorCode = sqlException.getErrorCode();
                BeeFactory beeFactory = BeeFactory.getInstance();
                String dbTypeInString = ((DruidDataSource) beeFactory.getDataSource()).getDbType();
                DbType dbType = DbType.valueOf(dbTypeInString);
                switch (dbType) {
                    // 如果是 MySQL 报错
                    case mysql -> {
                        switch (errorCode) {
                            case 1059 -> {//ER_TOO_LONG_IDENT -- Identifier name '%s' is too long
                                System.out.println("键名太长");
                            }
                            case 1060 -> {//ER_DUP_FIELDNAME -- Duplicate column name '%s'
                                System.out.println("字段名称重复（unique限制）");
                            }
                            case 1061 -> {//ER_DUP_KEYNAME -- Duplicate key name '%s'
                                System.out.println("键名重复");
                            }
                            case 1062 -> {//ER_DUP_ENTRY -- Duplicate entry '%s' for key %d
                                System.out.println("字段内容重复（unique限制）");
                                if (!(obj instanceof StringContent)) {
                                    break;
                                }
                                ObjectToSQLRich objectToSQLRich = new ObjectToSQLRich();
                                String sqlStringSelectGDR = objectToSQLRich.toSelectSQL(new GlobalDataRecord());
                                SQLStatement sqlStatement = SQLUtils.parseSingleStatement(sqlStringSelectGDR, DbType.mysql);
                                SQLStatement sqlStatement2 = new MySqlSelectIntoStatement();


                                // MoreTable moreTable = honeyFactory.getMoreTable();
                                // GlobalDataRecord crossQueryEntity = new GlobalDataRecord() {
                                //     @Getter
                                //     @Setter
                                //     @JoinTable(mainField="id", subField="global_id", joinType= JoinType.JOIN)
                                //     private StringContent stringContent = (StringContent) obj;
                                // };
                                // List<GlobalDataRecord> globalDataRecords = moreTable.select(crossQueryEntity);
                            }
                            case 1406 -> {// ER_DATA_TOO_LONG -- Data too long for column '%s' at row %ld
                                System.out.println("数据超出字段长度");
                            }
                            default -> {
                            }
                        }
                    }
                    default -> System.out.println("Not supported database.");
                }
            }
        }

        System.out.println(rowCount);
        return rowCount;
    }

}
