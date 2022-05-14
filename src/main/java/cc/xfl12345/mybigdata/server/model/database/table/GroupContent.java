package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;

/**
 * 表名：group_content
*/
@ToString
@Table(name = "`group_content`")
public class GroupContent implements Serializable {
    /**
     * 组id
     */
    @Column(name = "`group_id`")
    private Long groupId;

    /**
     * 组内对象的下标
     */
    @Column(name = "`item_index`")
    private Long itemIndex;

    /**
     * 组内对象
     */
    @Column(name = "`item`")
    private Long item;

    private static final long serialVersionUID = 1L;

    /**
     * 获取组id
     *
     * @return groupId - 组id
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * 设置组id
     *
     * @param groupId 组id
     */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取组内对象的下标
     *
     * @return itemIndex - 组内对象的下标
     */
    public Long getItemIndex() {
        return itemIndex;
    }

    /**
     * 设置组内对象的下标
     *
     * @param itemIndex 组内对象的下标
     */
    public void setItemIndex(Long itemIndex) {
        this.itemIndex = itemIndex;
    }

    /**
     * 获取组内对象
     *
     * @return item - 组内对象
     */
    public Long getItem() {
        return item;
    }

    /**
     * 设置组内对象
     *
     * @param item 组内对象
     */
    public void setItem(Long item) {
        this.item = item;
    }
}