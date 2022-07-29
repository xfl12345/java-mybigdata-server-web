package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.error.TableDataException;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;

import java.math.BigDecimal;

public interface NumberTypeHandler {
    /**
     * 插入数字，返回 全局数据记录表 的 ID
     * @return 全局数据记录表 的 ID
     */
    Long insert(BigDecimal value);

    Long selectId(BigDecimal value) throws TableOperationException;

    Long insertOrSelect4Id(BigDecimal value) throws Exception;

    // 更新数字 有可能是伪需求。因为数字是常量，如果需要更新，那也更多的是更新引用，而非修改内容。
    void updateById(BigDecimal value, Long globalId) throws TableOperationException, TableDataException;

    void delete(BigDecimal value) throws TableOperationException;
}
