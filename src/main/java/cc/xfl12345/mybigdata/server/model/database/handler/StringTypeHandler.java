package cc.xfl12345.mybigdata.server.model.database.handler;

import java.util.List;

public interface StringTypeHandler extends AppDataUnitHandler<String> {

    /**
     * 按 global_id 更新字符串
     * @param oldValue 旧内容
     * @param value 新内容
     */
    void updateStringByFullText(String oldValue, String value) throws Exception;

    /**
     * 按 前缀匹配 获取关于该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     *
     * @param prefix 待查找字符串
     * @return 返回结果包含 该字符串 位于 字符串表 的一行内容，以及 全局数据记录表 的一行内容
     */
    List<String> selectStringByPrefix(String prefix);
}
