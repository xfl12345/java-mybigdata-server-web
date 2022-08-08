package cc.xfl12345.mybigdata.server.model.database.table.pojo;

import java.io.Serializable;

/**
 * 表名：integer_content
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "integer_content")
@javax.persistence.Entity
public class IntegerContent implements Cloneable, Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    @io.swagger.annotations.ApiModelProperty("当前表所在数据库实例里的全局ID")
    @javax.persistence.Id
    private Long globalId;

    /**
     * 64位带符号的整型数字
     */
    @javax.persistence.Column(name = "content", nullable = false)
    @io.swagger.annotations.ApiModelProperty("64位带符号的整型数字")
    private Long content;

    private static final long serialVersionUID = 1L;

    @Override
    public IntegerContent clone() throws CloneNotSupportedException {
        return (IntegerContent) super.clone();
    }
}
