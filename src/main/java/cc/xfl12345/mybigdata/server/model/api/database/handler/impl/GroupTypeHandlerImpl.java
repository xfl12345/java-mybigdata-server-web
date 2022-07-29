package cc.xfl12345.mybigdata.server.model.api.database.handler.impl;

import cc.xfl12345.mybigdata.server.model.api.database.handler.GroupTypeHandler;
import cc.xfl12345.mybigdata.server.model.api.database.result.GroupTypeResult;
import cc.xfl12345.mybigdata.server.model.database.table.GroupRecord;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class GroupTypeHandlerImpl extends AbstractTableHandler implements GroupTypeHandler {


    @Override
    public GroupTypeResult insertNewGroup(String name, List<Long> groupItems) {
        Date date = new Date();
        GroupTypeResult result = new GroupTypeResult();

        GroupRecord groupRecord = new GroupRecord();


        // 开启事务
        // Transaction transaction = SessionFactory.getTransaction();
        // try {
        //     transaction.begin();
        //     transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
        //     HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
        //     SuidRich suid = honeyFactory.getSuidRich();
        //
        //
        //
        //     // suid.insert
        //     GlobalDataRecord globalDataRecord = globalDataRecordProducer.poll(5, TimeUnit.SECONDS);
        //     if (globalDataRecord == null) {
        //         transaction.rollback();
        //         result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_GET_GID_TIMEOUT);
        //         return result;
        //     }
        //
        //     // 触发 读 锁定（锁定单行）
        //     globalDataRecord = suid.selectById(new GlobalDataRecord(), globalDataRecord.getId());
        //     globalDataRecord.setCreateTime(date);
        //     globalDataRecord.setUpdateTime(date);
        //     globalDataRecord.setModifiedCount(1L);
        //     globalDataRecord.setTableName(
        //         coreTableCache.getTableNameCache().getValue(CoreTableNames.TABLE_NAME_STRING_CONTENT)
        //     );
        //     content.setGlobalId(globalDataRecord.getId());
        //
        //     // 插入数据
        //     int affectedRowCount = 0;
        //     affectedRowCount = suid.insert(content);
        //     if (affectedRowCount != 1) {
        //         transaction.rollback();
        //         result.setSimpleResult(SimpleCoreTableCurdResult.FAILED_OPERATION_REJECTED);
        //         result.setMessage("未知错误，插入字符串表失败。");
        //     } else {
        //         affectedRowCount = suid.update(globalDataRecord);
        //         if (affectedRowCount != 1) {
        //             transaction.rollback();
        //             result.setSimpleResult(SimpleCoreTableCurdResult.UNKNOWN_FAILED);
        //             result.setMessage("未知错误，更新全局记录表失败。");
        //         } else {
        //             transaction.commit();
        //             result.setGlobalDataRecord(globalDataRecord);
        //             result.setStringContent(content);
        //             result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
        //         }
        //     }
        // } catch (BeeException | InterruptedException | NullPointerException e) {
        //     sqlErrorHandler.defaultErrorHandler(e, transaction, result);
        // }

        return result;
    }

    @Override
    public GroupTypeResult insertExistGroupByGlobalId(Long globalId, List<Long> groupItems) {
        return null;
    }

    @Override
    public GroupTypeResult deleteGroupItemByGlobalId(Long globalId, List<Long> groupItems) {
        return null;
    }

    @Override
    public GroupTypeResult replaceAllGroupItemByGlobalId(Long globalId, List<Long> groupItems) {
        return null;
    }

    @Override
    public GroupTypeResult replaceGroupItemByGlobalId(Long globalId, List<Long> groupOldItems, List<Long> groupNewItems) {
        return null;
    }

    @Override
    public GroupTypeResult insertNewGroup(String name, Set<Long> groupItems) {
        return null;
    }

    @Override
    public GroupTypeResult insertExistGroupByGlobalId(Long globalId, Set<Long> groupItems) {
        return null;
    }

    @Override
    public GroupTypeResult deleteGroupItemByGlobalId(Long globalId, Set<Long> groupItems) {
        return null;
    }

    @Override
    public GroupTypeResult replaceAllGroupItemByGlobalId(Long globalId, Set<Long> groupItems) {
        return null;
    }

    @Override
    public GroupTypeResult replaceGroupItemByGlobalId(Long globalId, Set<Long> groupOldItems, Set<Long> groupNewItems) {
        return null;
    }
}
