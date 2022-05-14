package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;

/**
 * 表名：binary_relationship_record
*/
@ToString
@Table(name = "`binary_relationship_record`")
public class BinaryRelationshipRecord implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Column(name = "`global_id`")
    private Long globalId;

    /**
     * 对象A
     */
    @Column(name = "`item_a`")
    private Long itemA;

    /**
     * 对象B
     */
    @Column(name = "`item_b`")
    private Long itemB;

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
     * 获取对象A
     *
     * @return itemA - 对象A
     */
    public Long getItemA() {
        return itemA;
    }

    /**
     * 设置对象A
     *
     * @param itemA 对象A
     */
    public void setItemA(Long itemA) {
        this.itemA = itemA;
    }

    /**
     * 获取对象B
     *
     * @return itemB - 对象B
     */
    public Long getItemB() {
        return itemB;
    }

    /**
     * 设置对象B
     *
     * @param itemB 对象B
     */
    public void setItemB(Long itemB) {
        this.itemB = itemB;
    }
}