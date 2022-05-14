package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.ToString;

/**
 * 表名：view_binary_relationship_record
*/
@ToString
@Table(name = "`view_binary_relationship_record`")
public class ViewBinaryRelationshipRecord implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 关于某行数据的，整个MySQL数据库乃至全球唯一的真正的全局ID
     */
    @Column(name = "`uuid`")
    private String uuid;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 修改次数（版本迭代）
     */
    @Column(name = "`modified_count`")
    private Long modifiedCount;

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

    @Column(name = "`description`")
    private String description;

    @Column(name = "`item_a_description`")
    private String itemADescription;

    @Column(name = "`item_b_description`")
    private String itemBDescription;

    private static final long serialVersionUID = 1L;

    /**
     * 获取当前表所在数据库实例里的全局ID
     *
     * @return id - 当前表所在数据库实例里的全局ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置当前表所在数据库实例里的全局ID
     *
     * @param id 当前表所在数据库实例里的全局ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取关于某行数据的，整个MySQL数据库乃至全球唯一的真正的全局ID
     *
     * @return uuid - 关于某行数据的，整个MySQL数据库乃至全球唯一的真正的全局ID
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 设置关于某行数据的，整个MySQL数据库乃至全球唯一的真正的全局ID
     *
     * @param uuid 关于某行数据的，整个MySQL数据库乃至全球唯一的真正的全局ID
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * 获取创建时间
     *
     * @return createTime - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return updateTime - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取修改次数（版本迭代）
     *
     * @return modifiedCount - 修改次数（版本迭代）
     */
    public Long getModifiedCount() {
        return modifiedCount;
    }

    /**
     * 设置修改次数（版本迭代）
     *
     * @param modifiedCount 修改次数（版本迭代）
     */
    public void setModifiedCount(Long modifiedCount) {
        this.modifiedCount = modifiedCount;
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

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return itemADescription
     */
    public String getItemADescription() {
        return itemADescription;
    }

    /**
     * @param itemADescription
     */
    public void setItemADescription(String itemADescription) {
        this.itemADescription = itemADescription;
    }

    /**
     * @return itemBDescription
     */
    public String getItemBDescription() {
        return itemBDescription;
    }

    /**
     * @param itemBDescription
     */
    public void setItemBDescription(String itemBDescription) {
        this.itemBDescription = itemBDescription;
    }
}