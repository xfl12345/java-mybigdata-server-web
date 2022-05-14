package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;

/**
 * 表名：tree_struct_record
*/
@ToString
@Table(name = "`tree_struct_record`")
public class TreeStructRecord implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Column(name = "`global_id`")
    private Long globalId;

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
}