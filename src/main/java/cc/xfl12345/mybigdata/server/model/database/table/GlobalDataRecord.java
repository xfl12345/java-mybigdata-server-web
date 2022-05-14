package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.ToString;

/**
 * 表名：global_data_record
*/
@ToString
@Table(name = "`global_data_record`")
public class GlobalDataRecord implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Id
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
     * 该行数据所在的表名
     */
    @Column(name = "`table_name`")
    private Long tableName;

    /**
     * 该行数据的附加简述
     */
    @Column(name = "`description`")
    private Long description;

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
     * 获取该行数据所在的表名
     *
     * @return tableName - 该行数据所在的表名
     */
    public Long getTableName() {
        return tableName;
    }

    /**
     * 设置该行数据所在的表名
     *
     * @param tableName 该行数据所在的表名
     */
    public void setTableName(Long tableName) {
        this.tableName = tableName;
    }

    /**
     * 获取该行数据的附加简述
     *
     * @return description - 该行数据的附加简述
     */
    public Long getDescription() {
        return description;
    }

    /**
     * 设置该行数据的附加简述
     *
     * @param description 该行数据的附加简述
     */
    public void setDescription(Long description) {
        this.description = description;
    }
}