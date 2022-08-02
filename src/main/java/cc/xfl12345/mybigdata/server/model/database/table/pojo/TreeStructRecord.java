package cc.xfl12345.mybigdata.server.model.database.table.pojo;

import java.io.Serializable;

/**
 * 表名：tree_struct_record
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "tree_struct_record")
@javax.persistence.Entity
@org.teasoft.bee.osql.annotation.Table("tree_struct_record")
public class TreeStructRecord implements Cloneable, Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    @io.swagger.annotations.ApiModelProperty("当前表所在数据库实例里的全局ID")
    @javax.persistence.Id
    @org.teasoft.bee.osql.annotation.Column("global_id")
    @org.teasoft.bee.osql.annotation.PrimaryKey
    private Long globalId;

    /**
     * 根节点对象
     */
    @javax.persistence.Column(name = "root_id", nullable = false)
    @io.swagger.annotations.ApiModelProperty("根节点对象")
    @org.teasoft.bee.osql.annotation.Column("root_id")
    private Long rootId;

    /**
     * 整个树的节点个数
     */
    @javax.persistence.Column(name = "item_count", nullable = false)
    @io.swagger.annotations.ApiModelProperty("整个树的节点个数")
    @org.teasoft.bee.osql.annotation.Column("item_count")
    private Integer itemCount;

    /**
     * 整个树的深度（有几层）
     */
    @javax.persistence.Column(name = "tree_deep", nullable = false)
    @io.swagger.annotations.ApiModelProperty("整个树的深度（有几层）")
    @org.teasoft.bee.osql.annotation.Column("tree_deep")
    private Integer treeDeep;

    /**
     * JSON文本长度
     */
    @javax.persistence.Column(name = "content_length", nullable = false)
    @io.swagger.annotations.ApiModelProperty("JSON文本长度")
    @org.teasoft.bee.osql.annotation.Column("content_length")
    private Short contentLength;

    /**
     * 以JSON字符串形式记载树形结构
     */
    @javax.persistence.Column(name = "struct_data", nullable = false, length = 16000)
    @io.swagger.annotations.ApiModelProperty("以JSON字符串形式记载树形结构")
    @org.teasoft.bee.osql.annotation.Column("struct_data")
    private String structData;

    private static final long serialVersionUID = 1L;

    @Override
    public TreeStructRecord clone() throws CloneNotSupportedException {
        return (TreeStructRecord) super.clone();
    }
}
