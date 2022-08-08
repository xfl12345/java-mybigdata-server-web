package cc.xfl12345.mybigdata.server.model.database.table.pojo;

import java.io.Serializable;

/**
 * 表名：group_content
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "group_content")
@javax.persistence.Entity
public class GroupContent implements Cloneable, Serializable {
    /**
     * 组id
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    @io.swagger.annotations.ApiModelProperty("组id")
    @javax.persistence.Id
    private Long globalId;

    /**
     * 组内对象的下标
     */
    @javax.persistence.Column(name = "item_index", nullable = true)
    @io.swagger.annotations.ApiModelProperty("组内对象的下标")
    private Long itemIndex;

    /**
     * 组内对象
     */
    @javax.persistence.Column(name = "item", nullable = false)
    @io.swagger.annotations.ApiModelProperty("组内对象")
    private Long item;

    private static final long serialVersionUID = 1L;

    @Override
    public GroupContent clone() throws CloneNotSupportedException {
        return (GroupContent) super.clone();
    }
}
