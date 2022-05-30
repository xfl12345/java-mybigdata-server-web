package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.MultipleResultBase;
import cc.xfl12345.mybigdata.server.model.database.result.StringTypeResult;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;

public interface StringTypeHandler {
    /**
     * 插入字符串
     * @return 返回结果包含 该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     */
    StringTypeResult insertString(String value);

    /**
     * 按 global_id 更新字符串
     * @param value 新内容
     * @param globalId global_id
     * @return 返回结果只包含是否执行成功
     */
    StringTypeResult updateStringByGlobalId(String value, Long globalId);

    /**
     * 按 global_id 更新字符串
     * @param oldValue 旧内容
     * @param value 新内容
     * @return 返回结果只包含是否执行成功
     */
    StringTypeResult updateStringByGlobalId(String oldValue, String value);

    /**
     * 按 全文匹配 获取关于该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     * @param value 待查找字符串
     * @return 返回结果包含 该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     */
    StringTypeResult selectStringByFullText(String value);

    /**
     * 删除字符串（完整匹配）
     * @param value 完整字符串
     * @return 返回结果只包含是否执行成功
     */
    StringTypeResult deleteStringByFullText(String value);

    /**
     * 按 前缀匹配 获取关于该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     * @param prefix 待查找字符串
     * @return 返回结果包含 该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     */
    MultipleResultBase<StringContent> selectStringByPrefix(String prefix);
}
