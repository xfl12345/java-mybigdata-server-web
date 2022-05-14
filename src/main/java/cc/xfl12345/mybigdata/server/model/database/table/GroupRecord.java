package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;

/**
 * 表名：group_record
*/
@ToString
@Table(name = "`group_record`")
public class GroupRecord implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Column(name = "`global_id`")
    private Long globalId;

    /**
     * 组名
     */
    @Column(name = "`group_name`")
    private Long groupName;

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
     * 获取组名
     *
     * @return groupName - 组名
     */
    public Long getGroupName() {
        return groupName;
    }

    /**
     * 设置组名
     *
     * @param groupName 组名
     */
    public void setGroupName(Long groupName) {
        this.groupName = groupName;
    }
}