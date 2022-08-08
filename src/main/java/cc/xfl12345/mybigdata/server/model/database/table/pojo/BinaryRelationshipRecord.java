package cc.xfl12345.mybigdata.server.model.database.table.pojo;

import java.io.Serializable;

/**
 * 表名：binary_relationship_record
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "binary_relationship_record")
@javax.persistence.Entity
public class BinaryRelationshipRecord implements Cloneable, Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    @io.swagger.annotations.ApiModelProperty("当前表所在数据库实例里的全局ID")
    @javax.persistence.Id
    private Long globalId;

    /**
     * 对象A
     */
    @javax.persistence.Column(name = "item_a", nullable = false)
    @io.swagger.annotations.ApiModelProperty("对象A")
    private Long itemA;

    /**
     * 对象B
     */
    @javax.persistence.Column(name = "item_b", nullable = false)
    @io.swagger.annotations.ApiModelProperty("对象B")
    private Long itemB;

    private static final long serialVersionUID = 1L;

    @Override
    public BinaryRelationshipRecord clone() throws CloneNotSupportedException {
        return (BinaryRelationshipRecord) super.clone();
    }
}
