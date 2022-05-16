package cc.xfl12345.mybigdata.server.model.database;

import cc.xfl12345.mybigdata.server.appconst.SimpleJdbcResult;
import cc.xfl12345.mybigdata.server.model.database.producer.GlobalDataRecordProducer;
import cc.xfl12345.mybigdata.server.model.database.result.StringTypeResult;
import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.bee.osql.transaction.Transaction;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.SessionFactory;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CoreTableHandler implements DisposableBean {

    @Getter
    @Setter
    protected NoArgGenerator uuidGenerator;

    protected GlobalDataRecordProducer globalDataRecordProducer;
    protected boolean keepGoing = true;

    public CoreTableHandler() {
        this(Generators.timeBasedGenerator());
    }

    public CoreTableHandler(NoArgGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
        globalDataRecordProducer = new GlobalDataRecordProducer(uuidGenerator);


    }

    public UUID getNewUUID() {
        return uuidGenerator.generate();
    }





    public StringTypeResult insertString(String content) {
        StringTypeResult stringTypeResult = new StringTypeResult();

        // 预备一个 StringContent对象 空间
        StringContent stringContent = new StringContent();
        stringContent.setContent(content);
        stringContent.setContentLength((short) content.length());

        // 开启事务，防止 global_id 冲突
        Transaction transaction= SessionFactory.getTransaction();
        transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
        try {
            transaction.begin();
            SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();

            GlobalDataRecord globalDataRecord = null;
            while (keepGoing && globalDataRecord == null) {
                globalDataRecord = globalDataRecordProducer.poll(3, TimeUnit.SECONDS);
            }
            // 触发 读 锁定（锁定单行）
            globalDataRecord = suid.selectById(
                new GlobalDataRecord(),
                Objects.requireNonNull(globalDataRecord).getId()
            );
            stringContent.setGlobalId(globalDataRecord.getId());

            // 尝试插入数据
            int affectedRowCount = suid.insert(stringContent);

            // affectedRowCount 为零，说明字符串重复了
            if (affectedRowCount == 0) {
                stringTypeResult.setSimpleJdbcResult(SimpleJdbcResult.duplicate);
//                stringTypeResult.setGlobalDataRecord(
//                    suid.selectById(new StringContent(), )
//                );
            }


            transaction.commit();


        } catch (BeeException | InterruptedException e) {
            log.error(e.getMessage());
            transaction.rollback();
            Throwable cause = e.getCause();
            // 如果是 MySQL 报错
            if (cause instanceof MysqlDataTruncation) {
                MysqlDataTruncation mysqlDataTruncation = (MysqlDataTruncation) cause;
                int errorCode = mysqlDataTruncation.getErrorCode();
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

        return stringTypeResult;
    }

    @Override
    public void destroy() throws Exception {

    }
}
