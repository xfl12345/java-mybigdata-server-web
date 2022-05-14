package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;

/**
 * 表名：integer_content
*/
@ToString
@Table(name = "`integer_content`")
public class IntegerContent implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Column(name = "`global_id`")
    private Long globalId;

    /**
     * 64位带符号的整型数字
     */
    @Column(name = "`content`")
    private Long content;

    private static final long serialVersionUID = 1L;

    /**
     * 获取当前表所在数据库实例里的全局ID
     *
     * @return globalId - 当前表所在数据库实例里的全局ID
     */
    public Long getGlobalId() {
        return globalId;
    }

    /**
     * 设置当前表所在数据库实例里的全局ID
     *
     * @param globalId 当前表所在数据库实例里的全局ID
     */
    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    /**
     * 获取64位带符号的整型数字
     *
     * @return content - 64位带符号的整型数字
     */
    public Long getContent() {
        return content;
    }

    /**
     * 设置64位带符号的整型数字
     *
     * @param content 64位带符号的整型数字
     */
    public void setContent(Long content) {
        this.content = content;
    }
}