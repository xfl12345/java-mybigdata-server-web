package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.listener.ContextFinalizer;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.teasoft.bee.osql.Op;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.HoneyFactory;

import java.io.IOException;
import java.sql.SQLException;

public class StudyNutzDao {
    public static void main(String[] args) throws SQLException, IOException {
        DruidDataSource dataSource = TestLoadDataSource.getDataSource();

        BeeFactory beeFactory = BeeFactory.getInstance();
        beeFactory.setDataSource(dataSource);
        HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
        SQLSelectStatement gdrSqlStatement = (SQLSelectStatement) SQLUtils.parseSingleMysqlStatement(
            honeyFactory.getObjToSQLRich().toSelectSQL(new GlobalDataRecord())
        );
        MySqlSelectQueryBlock gdrSelectQueryBlock = (MySqlSelectQueryBlock)gdrSqlStatement.getSelect().getQuery();

        System.out.println(gdrSelectQueryBlock.getSelectList());

        TypeUtils.getMapping(Integer.class);


        // PropertyDescriptor[]  propertyDescriptors = BeanUtils.getPropertyDescriptors(GlobalDataRecord.class);
        BeanUtils.getters(GlobalDataRecord.class, method -> {
            System.out.println(method.getName());
        });





        // org.springframework.beans.BeanUtils.getPropertyDescriptors(GlobalDataRecord.class)[0]
        // honeyFactory.getMapSql()
        //
        // JavaBeanInfo javaBeanInfo = JavaBeanInfo.build(GlobalDataRecord.class, Object.class, PropertyNamingStrategy.CamelCase);

        // for (PropertyDescriptor property : propertyDescriptors) {
        //     System.out.println(property.getName());
        // }


        NutDao dao = new NutDao(dataSource);

        GlobalDataRecord globalDataRecord;

        // Sql sql = new NutSql("select AAA.table_name as AAA_table_name from global_data_record as AAA where AAA.id=10", new SqlCallback() {
        //     @Override
        //     public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
        //         return dao.getObject(GlobalDataRecord.class, rs, new FieldMatcher(), "AAA_");
        //     }
        // });
        // globalDataRecord = dao.execute(sql).getObject(GlobalDataRecord.class);

        Record record = dao.fetch("global_data_record", Cnd.where("id", Op.eq.getOperator(), 10));

        globalDataRecord = record.toPojo(GlobalDataRecord.class);


        ContextFinalizer.deregisterJdbcDriver(null);
    }
}
