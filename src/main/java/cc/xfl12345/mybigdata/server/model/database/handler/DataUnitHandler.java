package cc.xfl12345.mybigdata.server.model.database.handler;

import java.util.List;

public interface DataUnitHandler <IdType, ValueType> {
    /**
     * 插入数据，返回 全局数据记录表 的 ID
     * @return 全局数据记录表 的 ID
     */
    IdType insertAndReturnId(ValueType value) throws Exception;

    /**
     * 插入数据。失败则抛出异常。
     * @return 影响行数
     */
    long insert(ValueType value) throws Exception;

    /**
     * 插入数据。失败则抛出异常。
     * @return 影响行数
     */
    long insertBatch(List<ValueType> values) throws Exception;

    /**
     * 给定数据，返回 全局数据记录表 的 ID
     * @return 全局数据记录表 的 ID
     */
    IdType selectId(ValueType value) throws Exception;

    ValueType selectById(IdType globalId) throws Exception;


    /**
     * 插入或检索目标数据，返回 全局数据记录表 的 ID。适用于需要无脑获取 全局ID 的情况。
     * @return 全局数据记录表 的 ID
     */
    IdType insertOrSelect4Id(ValueType value) throws Exception;

    /**
     * 按 全局ID 更新数据。失败则抛出异常。
     */
    void updateById(ValueType value, IdType globalId) throws Exception;

    /**
     * 按 全局ID 删除数据。失败则抛出异常。
     */
    void deleteById(IdType globalId) throws Exception;
}
