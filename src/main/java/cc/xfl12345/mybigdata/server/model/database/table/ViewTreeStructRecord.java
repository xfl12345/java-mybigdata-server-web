package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.ToString;

/**
 * 表名：view_tree_struct_record
*/
@ToString
@Table(name = "`view_tree_struct_record`")
public class ViewTreeStructRecord implements Serializable {
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
     * 根节点对象
     */
    @Column(name = "`root_id`")
    private Long rootId;

    /**
     * 整个树的节点个数
     */
    @Column(name = "`item_count`")
    private Integer itemCount;

    /**
     * 整个树的深度（有几层）
     */
    @Column(name = "`tree_deep`")
    private Integer treeDeep;

    /**
     * JSON文本长度
     */
    @Column(name = "`content_length`")
    private Short contentLength;

    /**
     * 以JSON字符串形式记载树形结构
     */
    @Column(name = "`struct_data`")
    private String structData;

    @Column(name = "`description`")
    private String description;

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
     * 获取根节点对象
     *
     * @return rootId - 根节点对象
     */
    public Long getRootId() {
        return rootId;
    }

    /**
     * 设置根节点对象
     *
     * @param rootId 根节点对象
     */
    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    /**
     * 获取整个树的节点个数
     *
     * @return itemCount - 整个树的节点个数
     */
    public Integer getItemCount() {
        return itemCount;
    }

    /**
     * 设置整个树的节点个数
     *
     * @param itemCount 整个树的节点个数
     */
    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    /**
     * 获取整个树的深度（有几层）
     *
     * @return treeDeep - 整个树的深度（有几层）
     */
    public Integer getTreeDeep() {
        return treeDeep;
    }

    /**
     * 设置整个树的深度（有几层）
     *
     * @param treeDeep 整个树的深度（有几层）
     */
    public void setTreeDeep(Integer treeDeep) {
        this.treeDeep = treeDeep;
    }

    /**
     * 获取JSON文本长度
     *
     * @return contentLength - JSON文本长度
     */
    public Short getContentLength() {
        return contentLength;
    }

    /**
     * 设置JSON文本长度
     *
     * @param contentLength JSON文本长度
     */
    public void setContentLength(Short contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * 获取以JSON字符串形式记载树形结构
     *
     * @return structData - 以JSON字符串形式记载树形结构
     */
    public String getStructData() {
        return structData;
    }

    /**
     * 设置以JSON字符串形式记载树形结构
     *
     * @param structData 以JSON字符串形式记载树形结构
     */
    public void setStructData(String structData) {
        this.structData = structData;
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
}