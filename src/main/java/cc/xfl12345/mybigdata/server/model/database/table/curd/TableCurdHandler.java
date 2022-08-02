package cc.xfl12345.mybigdata.server.model.database.table.curd;

public interface TableCurdHandler <T> {
    /**
     * 插入数据，返回 全局数据记录表 的 ID
     * @return 全局数据记录表 的 ID
     */
    Long insert( T value) throws Exception;

    /**
     * 给定数据，返回 全局数据记录表 的 ID
     * @return 全局数据记录表 的 ID
     */
    Long selectId(T value) throws Exception;

    /**
     * 插入或检索目标数据，返回 全局数据记录表 的 ID。适用于需要无脑获取 全局ID 的情况。
     * @return 全局数据记录表 的 ID
     */
    Long insertOrSelect4Id(T value) throws Exception;

    /**
     * 按 全局ID 更新数据。失败则抛出异常。
     */
    void updateById(T value, Long globalId) throws Exception;

    /**
     * 按 全局ID 删除数据。失败则抛出异常。
     */
    void deleteById(T value, Long globalId) throws Exception;


}
