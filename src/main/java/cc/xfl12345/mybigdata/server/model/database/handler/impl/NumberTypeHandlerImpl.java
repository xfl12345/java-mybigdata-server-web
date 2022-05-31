package cc.xfl12345.mybigdata.server.model.database.handler.impl;


import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.model.database.association.IntegerContentAssociation;
import cc.xfl12345.mybigdata.server.model.database.handler.NumberTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.result.ExecuteResultBase;
import cc.xfl12345.mybigdata.server.model.database.result.NumberTypeResult;
import cc.xfl12345.mybigdata.server.model.database.result.StringTypeResult;
import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.IntegerContent;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.MoreTable;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.bee.osql.transaction.Transaction;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.HoneyFactory;
import org.teasoft.honey.osql.core.SessionFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NumberTypeHandlerImpl extends AbstractTableHandler implements NumberTypeHandler {
    @Getter
    @Setter
    protected volatile StringTypeHandler stringTypeHandler = null;

    @Override
    public NumberTypeResult insertNumber(BigDecimal number) {
        Date date = new Date();
        NumberTypeResult result = new NumberTypeResult();

        if (isLongInteger(number)) { // 如果是 long型 整数可表示的 数字
            IntegerContent content = new IntegerContent();
            content.setContent(number.longValue());

            Transaction transaction = SessionFactory.getTransaction();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            try {
                transaction.begin();
                HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
                SuidRich suid = honeyFactory.getSuidRich();

                GlobalDataRecord globalDataRecord = globalDataRecordProducer.poll(5, TimeUnit.SECONDS);
                if (globalDataRecord == null) {
                    transaction.rollback();
                    result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_GET_GID_TIMEOUT);
                    return result;
                }

                // 触发 读 锁定（锁定单行）
                globalDataRecord = suid.selectById(new GlobalDataRecord(), globalDataRecord.getId());
                globalDataRecord.setCreateTime(date);
                globalDataRecord.setUpdateTime(date);
                globalDataRecord.setModifiedCount(1L);
                globalDataRecord.setTableName(
                    coreTableCache.getTableNameCache().getValue(CoreTableNames.TABLE_NAME_INTEGER_CONTENT)
                );
                content.setGlobalId(globalDataRecord.getId());

                // 插入数据
                int affectedRowCount = 0;
                affectedRowCount = suid.insert(content);
                if (affectedRowCount != 1) {
                    transaction.rollback();
                    result.setSimpleResult(SimpleCoreTableCurdResult.UNKNOWN_FAILED);
                    result.setSqlException(new Exception("未知错误，插入整数表失败。"));
                } else {
                    affectedRowCount = suid.update(globalDataRecord);
                    if (affectedRowCount != 1) {
                        transaction.rollback();
                        result.setSimpleResult(SimpleCoreTableCurdResult.UNKNOWN_FAILED);
                        result.setSqlException(new Exception("未知错误，更新全局记录表失败。"));
                    } else {
                        transaction.commit();
                        result.setGlobalDataRecord(globalDataRecord);
                        result.setIntegerContent(content);
                        result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
                    }
                }
            } catch (BeeException | InterruptedException | NullPointerException e) {
                // log.error(e.getMessage());
                transaction.rollback();
                Throwable cause = e.getCause();
                if (cause instanceof SQLException sqlException) {
                    int errorCode = sqlException.getErrorCode();
                    result.setSqlException(sqlException);
                    BeeFactory beeFactory = BeeFactory.getInstance();
                    String dbTypeInString = ((DruidDataSource) beeFactory.getDataSource()).getDbType();
                    DbType dbType = DbType.valueOf(dbTypeInString);
                    switch (dbType) {
                        // 如果是 MySQL 报错
                        case mysql -> {
                            switch (errorCode) {
                                case 1062 -> {//ER_DUP_ENTRY -- Duplicate entry '%s' for key %d
                                    result.setSimpleResult(SimpleCoreTableCurdResult.DUPLICATE);
                                    // 既然重复了，那就把它查出来。
                                    NumberTypeResult query = selectNumber(number);
                                    if (query.getSimpleResult().equals(SimpleCoreTableCurdResult.SUCCEED)) {
                                        result.setGlobalDataRecord(query.getGlobalDataRecord());
                                        result.setNumber(query.getNumber());
                                    }
                                }
                                case 1406 -> {// ER_DATA_TOO_LONG -- Data too long for column '%s' at row %ld
                                    // System.out.println("数据超出字段长度");
                                    result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_OVER_FLOW);
                                }
                                default -> result.setSimpleResult(SimpleCoreTableCurdResult.UNKNOWN_FAILED);
                            }
                        }
                        default -> log.error("Not supported database.");
                    }
                } else {
                    result.setSqlException(e);
                }
            }
        } else { // 如果超出 long型 整数可表示的 数字范围，或者是浮点数，统一做字符串处理
            StringTypeResult stringTypeResult = stringTypeHandler.insertString(number.toPlainString());
            result.setSimpleResult(stringTypeResult.getSimpleResult());
            if (stringTypeResult.getSimpleResult().equals(SimpleCoreTableCurdResult.SUCCEED)) {
                result.setStringContent(stringTypeResult.getStringContent());
            } else {
                result.setSqlException(stringTypeResult.getSqlException());
            }
        }

        return result;
    }

    @Override
    public NumberTypeResult selectNumber(BigDecimal number) {
        NumberTypeResult result = new NumberTypeResult();

        if (isLongInteger(number)) {
            Transaction transaction = SessionFactory.getTransaction();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            try {
                transaction.begin();
                HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
                MoreTable moreTable = honeyFactory.getMoreTable();

                IntegerContentAssociation association = new IntegerContentAssociation();
                association.setContent(number.longValue());
                association = moreTable.select(association).get(0);
                result.setGlobalDataRecord(association.getGlobalDataRecords().get(0));

                transaction.commit();

                IntegerContent integerContent = new IntegerContent();
                integerContent.setGlobalId(association.getGlobalId());
                integerContent.setContent(association.getContent());

                result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
            } catch (Exception e) {
                defaultErrorHandler(e, transaction, result);
            }
        } else {
            StringTypeResult stringTypeResult = stringTypeHandler.selectStringByFullText(number.toPlainString());
            result.setSimpleResult(stringTypeResult.getSimpleResult());
            if (stringTypeResult.getSimpleResult().equals(SimpleCoreTableCurdResult.SUCCEED)) {
                result.setStringContent(stringTypeResult.getStringContent());
            } else {
                result.setSqlException(stringTypeResult.getSqlException());
            }
        }

        return result;
    }

    @Override
    public NumberTypeResult updateNumberByGlobalId(BigDecimal number, Long globalId) {
        NumberTypeResult result = new NumberTypeResult();

        // 开启事务，防止 global_id 冲突
        Transaction transaction = SessionFactory.getTransaction();
        transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
        try {
            transaction.begin();
            HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
            MoreTable moreTable = honeyFactory.getMoreTable();
            SuidRich suid = honeyFactory.getSuidRich();


            GlobalDataRecord gdrInDb = suid.selectById(new GlobalDataRecord(), globalId);
            switch (coreTableCache.getTableNameCache().getKey(gdrInDb.getTableName())) {
                case CoreTableNames.TABLE_NAME_STRING_CONTENT -> {
                    // TODO 实现 全局ID表 获取 JSON 基本数据类型
                }
                case CoreTableNames.TABLE_NAME_INTEGER_CONTENT -> {

                }
                default -> {
                    transaction.rollback();
                    result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_TYPE_ERROR);
                    result.setSqlException(new IllegalArgumentException("获取数字只能从"
                        + " \"" + CoreTableNames.TABLE_NAME_INTEGER_CONTENT + "\" "
                        + "或者"
                        + " \"" + CoreTableNames.TABLE_NAME_STRING_CONTENT + "\" "
                        + "这两个表中获取，数据库存储的数据有误。"
                    ));
                }
            }

            result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
        } catch (Exception e) {
            defaultErrorHandler(e, transaction, result);
        }

        return null;
    }

    @Override
    public NumberTypeResult deleteNumber(BigDecimal number) {
        return null;
    }

    public boolean isLongInteger(BigDecimal number) {
        return new BigDecimal(number.longValue()).equals(number);
    }

    protected void defaultErrorHandler(Exception e, Transaction transaction, ExecuteResultBase result) {
        // log.error(e.getMessage());
        transaction.rollback();
        if (e instanceof IndexOutOfBoundsException) {
            result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_NOT_FOUND);
        } else {
            result.setSqlException(e);
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
                            default -> result.setSimpleResult(SimpleCoreTableCurdResult.UNKNOWN_FAILED);
                        }
                    }
                    default -> log.error("Not supported database.");
                }
            }
        }
    }
}
