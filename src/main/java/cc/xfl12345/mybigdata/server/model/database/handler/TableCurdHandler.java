package cc.xfl12345.mybigdata.server.model.database.handler;

import dev.morphia.query.Query;
import org.bson.types.ObjectId;

import java.util.List;

public interface TableCurdHandler <T> {
    /**
     * 插入数据，返回 全局数据记录表 的 ID
     * @return 全局数据记录表 的 ID
     */
    ObjectId insert(T value) throws Exception;

    /**
     * 给定数据，返回 全局数据记录表 的 ID
     * @return 全局数据记录表 的 ID
     */
    ObjectId selectId(T value) throws Exception;

    T selectById(ObjectId globalId, String[] fields) throws Exception;

    T selectOne(T value, String[] fields) throws Exception;

    List<T> select(Query<T> condition) throws Exception;

    /**
     * 插入或检索目标数据，返回 全局数据记录表 的 ID。适用于需要无脑获取 全局ID 的情况。
     * @return 全局数据记录表 的 ID
     */
    ObjectId insertOrSelect4Id(T value) throws Exception;

    /**
     * 按 全局ID 更新数据。失败则抛出异常。
     */
    void updateById(T value, ObjectId globalId) throws Exception;

    /**
     * 按 全局ID 删除数据。失败则抛出异常。
     */
    void delete(T value) throws Exception;

    /**
     * 按 全局ID 删除数据。失败则抛出异常。
     */
    void deleteById(T value, ObjectId globalId) throws Exception;


}
