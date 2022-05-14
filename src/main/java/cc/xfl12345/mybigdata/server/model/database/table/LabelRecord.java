package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;

/**
 * 表名：label_record
*/
@ToString
@Table(name = "`label_record`")
public class LabelRecord implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Column(name = "`global_id`")
    private Long globalId;

    /**
     * 标签集合
     */
    @Column(name = "`group_id`")
    private Long groupId;

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
     * 获取标签集合
     *
     * @return groupId - 标签集合
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 设置标签集合
     *
     * @param groupId 标签集合
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}