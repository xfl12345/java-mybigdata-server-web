package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;

import java.util.List;

public interface StringTypeHandler {
    /**
     * 插入字符串，返回 全局数据记录表 的 ID
     * @return 全局数据记录表 的 ID
     */
    Long insert(String value);

    /**
     * 插入或查询字符串，返回 全局数据记录表 的 ID
     * @return 全局数据记录表 的 ID
     */
    Long insertOrSelect4Id(String value) throws Exception;

    /**
     * 按 global_id 更新字符串
     * @param value 新内容
     * @param globalId global_id
     */
    void updateStringByGlobalId(String value, Long globalId) throws TableOperationException;

    /**
     * 按 global_id 更新字符串
     * @param oldValue 旧内容
     * @param value 新内容
     */
    void updateStringByFullText(String oldValue, String value) throws TableOperationException;

    /**
     * 按 全文匹配 获取关于该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     * @param value 待查找字符串
     * @param fields 查询字段控制
     * @return 返回结果包含 该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     */
    StringContent selectStringByFullText(String value, String[] fields);

    /**
     * 按 前缀匹配 获取关于该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     * @param prefix 待查找字符串
     * @param fields 查询字段控制
     * @return 返回结果包含 该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     */
    List<StringContent> selectStringByPrefix(String prefix, String[] fields);

    StringContent selectById(Long globalId, String[] fields);

    Long selectId(String value);

    /**
     * 删除字符串（完整匹配）
     * @param value 完整字符串
     * @return 返回结果只包含是否执行成功
     */
    void deleteStringByFullText(String value) throws TableOperationException;

    void deleteById(Long globalId) throws TableOperationException;

    void delete(StringContent value) throws TableOperationException;
}
