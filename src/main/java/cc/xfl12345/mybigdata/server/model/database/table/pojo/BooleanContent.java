package cc.xfl12345.mybigdata.server.model.database.table.pojo;

import dev.morphia.annotations.IndexOptions;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * 表名：boolean_content
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "boolean_content")
@javax.persistence.Entity
@dev.morphia.annotations.Entity("boolean_content")
public class BooleanContent implements Cloneable, Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    @io.swagger.annotations.ApiModelProperty("当前表所在数据库实例里的全局ID")
    @javax.persistence.Id
    @dev.morphia.annotations.Id
    private ObjectId globalId;

    /**
     * 布尔值
     */
    @javax.persistence.Column(name = "content", nullable = false)
    @io.swagger.annotations.ApiModelProperty("布尔值")
    @dev.morphia.annotations.Indexed(options = @IndexOptions(unique = true))
    private Boolean content;

    private static final long serialVersionUID = 1L;

    @Override
    public BooleanContent clone() throws CloneNotSupportedException {
        return (BooleanContent) super.clone();
    }
}
