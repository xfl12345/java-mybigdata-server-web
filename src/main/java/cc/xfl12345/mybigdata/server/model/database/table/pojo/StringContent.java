package cc.xfl12345.mybigdata.server.model.database.table.pojo;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import dev.morphia.annotations.IndexOptions;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * 表名：string_content
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = CoreTableNames.STRING_CONTENT)
@javax.persistence.Entity
@dev.morphia.annotations.Entity(value = CoreTableNames.STRING_CONTENT)
public class StringContent implements Cloneable, Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    @io.swagger.annotations.ApiModelProperty("当前表所在数据库实例里的全局ID")
    @javax.persistence.Id
    @dev.morphia.annotations.Indexed(options = @IndexOptions(unique = true))
    @dev.morphia.annotations.Id
    private ObjectId globalId;

    /**
     * 字符串内容，最大长度为 768 个字符
     */
    @javax.persistence.Column(name = "content", nullable = false, length = 768)
    @io.swagger.annotations.ApiModelProperty("字符串内容，最大长度为 768 个字符")
    @dev.morphia.annotations.Indexed(options = @IndexOptions(unique = true))
    private String content;

    private static final long serialVersionUID = 1L;

    @Override
    public StringContent clone() throws CloneNotSupportedException {
        return (StringContent) super.clone();
    }
}
