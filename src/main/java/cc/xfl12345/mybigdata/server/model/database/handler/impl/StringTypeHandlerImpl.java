package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.model.database.association.StringContentAssociation;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.result.ExecuteResultBase;
import cc.xfl12345.mybigdata.server.model.database.result.MultipleResultBase;
import cc.xfl12345.mybigdata.server.model.database.result.StringTypeResult;
import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.MoreTable;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.bee.osql.transaction.Transaction;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;
import org.teasoft.honey.osql.core.HoneyFactory;
import org.teasoft.honey.osql.core.SessionFactory;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class StringTypeHandlerImpl extends AbstractTableHandler implements StringTypeHandler {
    @Override
    public StringTypeResult insertString(String value) {
        Date date = new Date();
        StringTypeResult result = new StringTypeResult();

        // 预备一个 StringContent对象 空间
        StringContent content = new StringContent();
        content.setContent(value);
        content.setContentLength((short) value.length());

        // 开启事务，防止 global_id 冲突
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
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
                coreTableCache.getTableNameCache().getValue(CoreTableNames.TABLE_NAME_STRING_CONTENT)
            );
            content.setGlobalId(globalDataRecord.getId());

            // 插入数据
            int affectedRowCount = 0;
            affectedRowCount = suid.insert(content);
            if (affectedRowCount != 1) {
                transaction.rollback();
                result.setSimpleResult(SimpleCoreTableCurdResult.UNKNOWN_FAILED);
                result.setSqlException(new Exception("未知错误，插入字符串表失败。"));
            } else {
                affectedRowCount = suid.update(globalDataRecord);
                if (affectedRowCount != 1) {
                    transaction.rollback();
                    result.setSimpleResult(SimpleCoreTableCurdResult.UNKNOWN_FAILED);
                    result.setSqlException(new Exception("未知错误，更新全局记录表失败。"));
                } else {
                    transaction.commit();
                    result.setGlobalDataRecord(globalDataRecord);
                    result.setStringContent(content);
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
                                // TODO 完善接口定义
                                StringTypeResult query = selectStringByFullText(value, null);
                                if (query.getSimpleResult().equals(SimpleCoreTableCurdResult.SUCCEED)) {
                                    result.setGlobalDataRecord(query.getGlobalDataRecord());
                                    result.setStringContent(query.getStringContent());
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

        return result;
    }

    @Override
    public StringTypeResult updateStringByGlobalId(String value, Long globalId) {
        Date nowTime = new Date();
        StringTypeResult result = new StringTypeResult();

        // 开启事务，防止 global_id 冲突
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
            SuidRich suid = honeyFactory.getSuidRich();

            MoreTable moreTable = honeyFactory.getMoreTable();
            StringContentAssociation data2search = new StringContentAssociation();
            data2search.setGlobalId(globalId);
            StringContentAssociation dataInDb = moreTable.select(data2search).get(0);

            StringContent data2update = new StringContent();
            data2update.setGlobalId(globalId);
            data2update.setContent(value);
            data2update.setContentLength((short) value.length());

            GlobalDataRecord gdrDataInDb = dataInDb.getGlobalDataRecords().get(0);

            GlobalDataRecord gdrData2update = new GlobalDataRecord();
            gdrData2update.setId(gdrDataInDb.getId());
            gdrData2update.setUpdateTime(nowTime);
            gdrData2update.setModifiedCount(gdrDataInDb.getModifiedCount() + 1);

            // 更新数据
            int affectedRowCount = 0;
            // 更新 全局ID表
            affectedRowCount = suid.update(gdrData2update);
            if (affectedRowCount == 0) {
                transaction.rollback();
                result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_NOT_FOUND);
            } else {
                // 更新 字符串表
                affectedRowCount = suid.update(data2update);
                if (affectedRowCount == 0) {
                    transaction.rollback();
                    result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_NOT_FOUND);
                } else {
                    transaction.commit();
                    result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
                    result.setStringContent(data2update);
                }
            }
        } catch (Exception e) {
            defaultErrorHandler(e, transaction, result);
        }

        return result;
    }

    public StringTypeResult updateStringByFullText(String oldValue, String value) {
        Date nowTime = new Date();
        StringTypeResult result = new StringTypeResult();
        // TODO 完善接口定义
        StringTypeResult query = selectStringByFullText(oldValue, null);
        if (!query.getSimpleResult().equals(SimpleCoreTableCurdResult.SUCCEED)) {
            return query;
        }

        // 开启事务，防止 global_id 冲突
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
            SuidRich suid = honeyFactory.getSuidRich();

            GlobalDataRecord gdrDataInDb = query.getGlobalDataRecord();

            StringContent data2update = new StringContent();
            data2update.setGlobalId(gdrDataInDb.getId());
            data2update.setContent(value);
            data2update.setContentLength((short) value.length());

            GlobalDataRecord gdrData2update = new GlobalDataRecord();
            gdrData2update.setId(gdrDataInDb.getId());
            gdrData2update.setUpdateTime(nowTime);
            gdrData2update.setModifiedCount(gdrDataInDb.getModifiedCount() + 1);

            // 更新数据
            int affectedRowCount = 0;
            // 更新 全局ID表
            affectedRowCount = suid.update(gdrData2update);
            if (affectedRowCount == 0) {
                transaction.rollback();
                result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_NOT_FOUND);
            } else {
                // 更新 字符串表
                affectedRowCount = suid.update(data2update);
                if (affectedRowCount == 0) {
                    transaction.rollback();
                    result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_NOT_FOUND);
                } else {
                    transaction.commit();
                    result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
                    result.setStringContent(data2update);
                }
            }
        } catch (Exception e) {
            defaultErrorHandler(e, transaction, result);
        }

        return result;
    }

    @Override
    public StringTypeResult selectStringByFullText(String value, String[] fields) {
        StringTypeResult result = new StringTypeResult();

        StringContentAssociation association = new StringContentAssociation();
        association.setContent(value);

        Condition condition = new ConditionImpl();
        if (fields != null) {
            condition.selectField(fields);
        }

        // 开启事务，防止 global_id 冲突
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
            MoreTable moreTable = honeyFactory.getMoreTable();

            // 查询数据
            association = moreTable.select(association, condition).get(0);
            result.setGlobalDataRecord(association.getGlobalDataRecords().get(0));

            transaction.commit();

            StringContent stringContent = new StringContent();
            stringContent.setGlobalId(association.getGlobalId());
            stringContent.setContent(association.getContent());
            stringContent.setContentLength(association.getContentLength());
            stringContent.setDataFormat(association.getDataFormat());
            result.setStringContent(stringContent);

            result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
        } catch (Exception e) {
            defaultErrorHandler(e, transaction, result);
        }

        return result;
    }

    @Override
    public StringTypeResult deleteStringByFullText(String value) {
        StringTypeResult result = new StringTypeResult();

        // 预备一个 StringContent对象 空间
        StringContent searchStringContent = new StringContent();
        searchStringContent.setContent(value);

        // 开启事务，防止 global_id 冲突
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
            SuidRich suid = honeyFactory.getSuidRich();

            // 删除数据
            int affectedRowCount = 0;
            affectedRowCount = suid.delete(searchStringContent);
            if (affectedRowCount == 0) {
                result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_NOT_FOUND);
            } else {
                result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
            }
            result.setStringContent(searchStringContent);
            transaction.commit();
        } catch (Exception e) {
            defaultErrorHandler(e, transaction, result);
        }

        return result;
    }

    @Override
    public MultipleResultBase<StringContent> selectStringByPrefix(String prefix, String[] fields) {
        // TODO 实现前缀匹配
        return null;
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
