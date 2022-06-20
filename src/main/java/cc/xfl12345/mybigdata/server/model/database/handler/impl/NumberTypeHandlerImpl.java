package cc.xfl12345.mybigdata.server.model.database.handler.impl;


import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.model.database.association.IntegerContentAssociation;
import cc.xfl12345.mybigdata.server.model.database.constant.GlobalDataRecordConstant;
import cc.xfl12345.mybigdata.server.model.database.constant.IntegerContentConstant;
import cc.xfl12345.mybigdata.server.model.database.constant.StringContentConstant;
import cc.xfl12345.mybigdata.server.model.database.handler.NumberTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.result.NumberTypeResult;
import cc.xfl12345.mybigdata.server.model.database.result.ResultUtils;
import cc.xfl12345.mybigdata.server.model.database.result.StringTypeResult;
import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.IntegerContent;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.teasoft.bee.osql.*;
import org.teasoft.bee.osql.transaction.Transaction;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;
import org.teasoft.honey.osql.core.HoneyFactory;
import org.teasoft.honey.osql.core.SessionFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NumberTypeHandlerImpl extends AbstractTableHandler implements NumberTypeHandler {
    String messageInteger = "整数";
    String messageString = "字符串";
    String messageOperationInsert = "插入";
    String messageOperationUpdate = "更新";
    String messageOperationQuery = "查询";
    String messageOperationDelete = "删除";

    String messageTemplateUpdateOneTypeButContentIsAnotherType = "更新的新内容是%s，但旧内容是%s。";
    String messageTemplateNoUpdateBecauseSomeOperationFailed = "%s失败，不予以更新。";
    String messageTemplateShouldBeOneRowAffectedButActuallyNot = "理应%s 1 行记录，但实际上%s %d 行记录";
    String messageAlreadyRollbackNotUpdate = "已回滚操作，不予以更新。";

    @Getter
    @Setter
    protected volatile StringTypeHandler stringTypeHandler = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (stringTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("stringTypeHandler"));
        }
    }

    protected NumberTypeResult insertInteger(Long number) {
        Date date = new Date();
        NumberTypeResult result = new NumberTypeResult();

        IntegerContent content = new IntegerContent();
        content.setContent(number);

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
                coreTableCache.getTableNameCache().getValue(CoreTableNames.TABLE_NAME_INTEGER_CONTENT)
            );
            content.setGlobalId(globalDataRecord.getId());

            // 插入数据
            int affectedRowCount = 0;
            affectedRowCount = suid.insert(content);
            if (affectedRowCount != 1) {
                transaction.rollback();
                result.setSimpleResult(SimpleCoreTableCurdResult.UNKNOWN_FAILED);
                result.setMessage("未知错误，插入整数表失败。");
            } else {
                affectedRowCount = suid.update(globalDataRecord);
                if (affectedRowCount != 1) {
                    transaction.rollback();
                    result.setSimpleResult(SimpleCoreTableCurdResult.UNKNOWN_FAILED);
                    result.setMessage("未知错误，更新全局记录表失败。");
                } else {
                    transaction.commit();
                    result.setGlobalDataRecord(globalDataRecord);
                    result.setNumber(content);
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
                                NumberTypeResult query = selectNumber(new BigDecimal(number));
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
                    default -> log.error(e.getMessage());
                }
            } else {
                result.setSqlException(e);
            }
        }

        return result;
    }

    protected NumberTypeResult insertNumberAsString(BigDecimal number) {
        NumberTypeResult result = new NumberTypeResult();
        StringTypeResult stringTypeResult = stringTypeHandler.insertString(number.toPlainString());
        result.setSimpleResult(stringTypeResult.getSimpleResult());
        result.setSqlException(stringTypeResult.getSqlException());
        if (stringTypeResult.getStringContent() != null) {
            result.setNumber(stringTypeResult.getStringContent());
        }
        return result;
    }

    @Override
    public NumberTypeResult insertNumber(BigDecimal number) {
        if (isLongInteger(number)) { // 如果是 long型 整数可表示的 数字
            return insertInteger(number.longValue());
        } else { // 如果超出 long型 整数可表示的 数字范围，或者是浮点数，统一做字符串处理
            return insertNumberAsString(number);
        }
    }

    @Override
    public NumberTypeResult selectNumber(BigDecimal number) {
        NumberTypeResult result = new NumberTypeResult();

        if (isLongInteger(number)) {
            Transaction transaction = SessionFactory.getTransaction();
            try {
                transaction.begin();
                transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
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
                sqlErrorHandler.defaultErrorHandler(e, transaction, result);
            }
        } else {
            StringTypeResult stringTypeResult = stringTypeHandler.selectStringByFullText(
                number.toPlainString(),
                new String[]{StringContentConstant.DB_CONTENT}
            );
            ResultUtils.copyResultBase(stringTypeResult, result);
            if (stringTypeResult.getSimpleResult().equals(SimpleCoreTableCurdResult.SUCCEED)) {
                result.setNumber(stringTypeResult.getStringContent());
            }
        }

        return result;
    }

    @Override
    public NumberTypeResult updateNumberByGlobalId(BigDecimal number, Long globalId) {
        Date date = new Date();
        NumberTypeResult result = new NumberTypeResult();


        boolean isInteger = isLongInteger(number);
        String numberInString = number.toPlainString();

        // 开启事务，防止 global_id 冲突
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
            SuidRich suid = honeyFactory.getSuidRich();

            GlobalDataRecord gdrInDb = suid.selectById(new GlobalDataRecord(), globalId);
            switch (coreTableCache.getTableNameCache().getKey(gdrInDb.getTableName())) {
                case CoreTableNames.TABLE_NAME_STRING_CONTENT -> {
                    // 如果是内容是 整数，但旧内容又位于 字符串表
                    if (isInteger) {
                        // 格式化出错之后帮助信息的前缀语
                        String messageUpdateOneTypeButContentIsAnotherType =
                            messageTemplateUpdateOneTypeButContentIsAnotherType.formatted(messageInteger, messageString);

                        StringContent stringContent = new StringContent();
                        stringContent.setGlobalId(globalId);
                        int affectedRowCount = 0;
                        // 那就先尝试删除。
                        try {
                            affectedRowCount = suid.delete(stringContent);
                            if (affectedRowCount == 1) { // 删除成功了，那就插入新内容到 整数表
                                IntegerContent content = new IntegerContent();
                                content.setContent(number.longValue());
                                content.setGlobalId(gdrInDb.getId());

                                // 插入数据
                                try {
                                    affectedRowCount = suid.insert(content);
                                    // 如果插入成功了
                                    if (affectedRowCount == 1) {
                                        // 那就开始更新 全局记录表
                                        GlobalDataRecord gdr4update = gdrInDb.clone();
                                        // 更新修改时间
                                        gdr4update.setUpdateTime(date);
                                        // 更新修改次数
                                        gdr4update.setModifiedCount(gdrInDb.getModifiedCount() + 1);
                                        // 字符串表 替换成 整数表
                                        gdr4update.setTableName(
                                            coreTableCache.getTableNameCache().getValue(CoreTableNames.TABLE_NAME_INTEGER_CONTENT)
                                        );
                                        Condition condition = new ConditionImpl();
                                        condition.op(GlobalDataRecordConstant.DB_ID, Op.eq, gdr4update.getId());

                                        try {
                                            // 更新数据
                                            affectedRowCount = suid.updateById(gdr4update, condition);
                                            if (affectedRowCount == 1) { // 更新成功
                                                transaction.commit();
                                                result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
                                            } else {
                                                // 任务失败，终止交易
                                                transaction.rollback();
                                                result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED);
                                                result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                                    messageTemplateShouldBeOneRowAffectedButActuallyNot
                                                        .formatted(messageOperationUpdate, messageOperationUpdate, affectedRowCount) +
                                                    messageAlreadyRollbackNotUpdate
                                                );
                                            }
                                        } catch (BeeException e) {
                                            sqlErrorHandler.defaultErrorHandler(e, transaction, result);
                                            if (result.getSimpleResult() == SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED) {
                                                result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                                    messageTemplateNoUpdateBecauseSomeOperationFailed.formatted(messageOperationUpdate + messageString)
                                                );
                                            }
                                        }


                                    } else {
                                        // 任务失败，终止交易
                                        transaction.rollback();
                                        result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED);
                                        result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                            messageTemplateShouldBeOneRowAffectedButActuallyNot
                                                .formatted(messageOperationInsert, messageOperationInsert, affectedRowCount) +
                                            messageAlreadyRollbackNotUpdate
                                        );
                                    }
                                } catch (BeeException e) {
                                    sqlErrorHandler.defaultErrorHandler(e, transaction, result);
                                    if (result.getSimpleResult() == SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED) {
                                        result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                            messageTemplateNoUpdateBecauseSomeOperationFailed.formatted(messageOperationInsert + messageString)
                                        );
                                    }
                                }

                            } else {
                                // 任务失败，终止交易
                                transaction.rollback();
                                result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED);
                                result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                    messageTemplateShouldBeOneRowAffectedButActuallyNot
                                        .formatted(messageOperationDelete, messageOperationDelete, affectedRowCount) +
                                    messageAlreadyRollbackNotUpdate
                                );
                            }
                        } catch (BeeException e) {
                            sqlErrorHandler.defaultErrorHandler(e, transaction, result);
                            if (result.getSimpleResult() == SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED) {
                                result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                    messageTemplateNoUpdateBecauseSomeOperationFailed.formatted(messageOperationDelete + messageString)
                                );
                            }
                        }


                    } else {
                        StringTypeResult stringTypeResult = stringTypeHandler
                            .updateStringByGlobalId(number.toPlainString(), globalId);
                        ResultUtils.copyResultBase(stringTypeResult, result);
                    }
                }


                case CoreTableNames.TABLE_NAME_INTEGER_CONTENT -> {
                    // 内容是整数，同时旧内容也在整数表里
                    if (isInteger) {
                        IntegerContent integerContent = new IntegerContent();
                        integerContent.setGlobalId(globalId);
                        integerContent.setContent(number.longValue());

                        ConditionImpl condition = new ConditionImpl();
                        condition.op(IntegerContentConstant.GLOBAL_ID, Op.eq, globalId);


                        int affectedRowCount = 0;
                        try {
                            affectedRowCount = suid.updateById(integerContent, condition);
                            if (affectedRowCount == 1) {
                                transaction.commit();
                                result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
                            } else {
                                transaction.rollback();
                                result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED);
                                result.setMessage(messageTemplateShouldBeOneRowAffectedButActuallyNot
                                    .formatted(messageOperationUpdate, messageOperationUpdate, affectedRowCount) +
                                    messageAlreadyRollbackNotUpdate
                                );
                            }
                        } catch (Exception e) {
                            sqlErrorHandler.defaultErrorHandler(e, transaction, result);
                        }
                    } else {// 如果是内容是 字符串，但旧内容又位于 整数表
                        // 格式化出错之后帮助信息的前缀语
                        String messageUpdateOneTypeButContentIsAnotherType =
                            messageTemplateUpdateOneTypeButContentIsAnotherType.formatted(messageString, messageInteger);


                        IntegerContent integerContent = new IntegerContent();
                        integerContent.setGlobalId(globalId);
                        int affectedRowCount = 0;
                        // 那就先尝试删除。
                        try {
                            affectedRowCount = suid.delete(integerContent);
                            if (affectedRowCount == 1) { // 删除成功了，那就插入新内容到 字符串表
                                StringContent content = new StringContent();
                                content.setContent(numberInString);
                                content.setGlobalId(gdrInDb.getId());

                                // 插入数据
                                try {
                                    affectedRowCount = suid.insert(content);
                                    // 如果插入成功了
                                    if (affectedRowCount == 1) {
                                        // 那就开始更新 全局记录表
                                        GlobalDataRecord gdr4update = gdrInDb.clone();
                                        // 更新修改时间
                                        gdr4update.setUpdateTime(date);
                                        // 更新修改次数
                                        gdr4update.setModifiedCount(gdrInDb.getModifiedCount() + 1);
                                        // 整数表 替换成 字符串表
                                        gdr4update.setTableName(
                                            coreTableCache.getTableNameCache().getValue(CoreTableNames.TABLE_NAME_STRING_CONTENT)
                                        );
                                        Condition condition = new ConditionImpl();
                                        condition.op(GlobalDataRecordConstant.DB_ID, Op.eq, gdr4update.getId());

                                        try {
                                            // 更新数据
                                            affectedRowCount = suid.updateById(gdr4update, condition);
                                            if (affectedRowCount == 1) { // 更新成功
                                                transaction.commit();
                                                result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
                                            } else {
                                                // 任务失败，终止交易
                                                transaction.rollback();
                                                result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED);
                                                result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                                    messageTemplateShouldBeOneRowAffectedButActuallyNot
                                                        .formatted(messageOperationUpdate, messageOperationUpdate, affectedRowCount) +
                                                    messageAlreadyRollbackNotUpdate
                                                );
                                            }
                                        } catch (BeeException e) {
                                            sqlErrorHandler.defaultErrorHandler(e, transaction, result);
                                            if (result.getSimpleResult() == SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED) {
                                                result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                                    messageTemplateNoUpdateBecauseSomeOperationFailed.formatted(messageOperationUpdate + messageInteger)
                                                );
                                            }
                                        }


                                    } else {
                                        // 任务失败，终止交易
                                        transaction.rollback();
                                        result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED);
                                        result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                            messageTemplateShouldBeOneRowAffectedButActuallyNot
                                                .formatted(messageOperationInsert, messageOperationInsert, affectedRowCount) +
                                            messageAlreadyRollbackNotUpdate
                                        );
                                    }
                                } catch (BeeException e) {
                                    sqlErrorHandler.defaultErrorHandler(e, transaction, result);
                                    if (result.getSimpleResult() == SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED) {
                                        result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                            messageTemplateNoUpdateBecauseSomeOperationFailed.formatted(messageOperationInsert + messageInteger)
                                        );
                                    }
                                }

                            } else {
                                // 任务失败，终止交易
                                transaction.rollback();
                                result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED);
                                result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                    messageTemplateShouldBeOneRowAffectedButActuallyNot
                                        .formatted(messageOperationDelete, messageOperationDelete, affectedRowCount) +
                                    messageAlreadyRollbackNotUpdate
                                );
                            }
                        } catch (BeeException e) {
                            sqlErrorHandler.defaultErrorHandler(e, transaction, result);
                            if (result.getSimpleResult() == SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED) {
                                result.setMessage(messageUpdateOneTypeButContentIsAnotherType +
                                    messageTemplateNoUpdateBecauseSomeOperationFailed.formatted(messageOperationDelete + messageInteger)
                                );
                            }
                        }
                    }

                }
                default -> {
                    transaction.rollback();
                    result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_TYPE_ERROR);
                    result.setMessage("获取数字只能从"
                        + " \"" + CoreTableNames.TABLE_NAME_INTEGER_CONTENT + "\" "
                        + "或者"
                        + " \"" + CoreTableNames.TABLE_NAME_STRING_CONTENT + "\" "
                        + "这两个表中获取，数据库存储的数据有误。"
                    );
                }
            }

            result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
        } catch (Exception e) {
            sqlErrorHandler.defaultErrorHandler(e, transaction, result);
        }

        return result;
    }

    @Override
    public NumberTypeResult deleteNumber(BigDecimal number) {
        NumberTypeResult result = new NumberTypeResult();

        if (isLongInteger(number)) {
            Transaction transaction = SessionFactory.getTransaction();
            try {
                transaction.begin();
                transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
                HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
                SuidRich suid = honeyFactory.getSuidRich();

                IntegerContent integerContent = new IntegerContent();
                integerContent.setContent(number.longValue());

                int affectedRowCount = 0;
                affectedRowCount = suid.delete(integerContent);
                if (affectedRowCount == 1) {
                    transaction.commit();
                    result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
                } else if (affectedRowCount == 0) {
                    transaction.rollback();
                    result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_NOT_FOUND);
                } else {
                    transaction.rollback();
                    result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED);
                    result.setMessage(
                        messageTemplateShouldBeOneRowAffectedButActuallyNot
                            .formatted(messageOperationDelete, messageOperationDelete, affectedRowCount) +
                            messageAlreadyRollbackNotUpdate
                    );
                }
            } catch (Exception e) {
                sqlErrorHandler.defaultErrorHandler(e, transaction, result);
            }
        } else {
            StringTypeResult stringTypeResult = stringTypeHandler.deleteStringByFullText(number.toPlainString());
            ResultUtils.copyResultBase(stringTypeResult, result);
        }

        return result;
    }

    public boolean isLongInteger(BigDecimal number) {
        return number.scale() == 0 && new BigDecimal(number.longValue()).compareTo(number) == 0;
    }
}
