package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;

/**
 * 表名：integer_content
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "integer_content")
@javax.persistence.Entity
@org.teasoft.bee.osql.annotation.Table("integer_content")
public class IntegerContent implements Serializable {
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
     * 64位带符号的整型数字
     */
    @javax.persistence.Column(name = "content", nullable = false)
    @io.swagger.annotations.ApiModelProperty("64位带符号的整型数字")
    @org.teasoft.bee.osql.annotation.Column("content")
    private Long content;

    private static final long serialVersionUID = 1L;
}
