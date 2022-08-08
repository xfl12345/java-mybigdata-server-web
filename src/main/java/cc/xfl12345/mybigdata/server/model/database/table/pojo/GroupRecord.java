package cc.xfl12345.mybigdata.server.model.database.table.pojo;

import java.io.Serializable;

/**
 * 表名：group_record
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "group_record")
@javax.persistence.Entity
public class GroupRecord implements Cloneable, Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    @io.swagger.annotations.ApiModelProperty("当前表所在数据库实例里的全局ID")
    @javax.persistence.Id
    private Long globalId;

    /**
     * 组名
     */
    @javax.persistence.Column(name = "group_name", nullable = false)
    @io.swagger.annotations.ApiModelProperty("组名")
    private Long groupName;

    private static final long serialVersionUID = 1L;

    @Override
    public GroupRecord clone() throws CloneNotSupportedException {
        return (GroupRecord) super.clone();
    }
}
