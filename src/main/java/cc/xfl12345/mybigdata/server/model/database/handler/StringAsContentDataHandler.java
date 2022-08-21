package cc.xfl12345.mybigdata.server.model.database.handler;

import java.util.List;

public interface StringAsContentDataHandler<IdType, ValueType> extends DataHandler<IdType, ValueType> {
    /**
     * 按 全文匹配 替换 旧内容
     * @param oldValue 旧内容
     * @param value 新内容
     */
    void updateByFullText(String oldValue, String value) throws Exception;

    /**
     * 按 前缀匹配 检索字符串
     *
     * @param prefix 前缀
     */
    List<String> selectByPrefix(String prefix);
}
