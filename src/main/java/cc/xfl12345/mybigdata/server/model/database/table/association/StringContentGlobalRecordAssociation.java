package cc.xfl12345.mybigdata.server.model.database.table.association;

import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;

import java.io.Serializable;
import java.util.List;

/**
 * 表名：string_content
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "string_content")
@org.teasoft.bee.osql.annotation.Table("string_content")
public class StringContentGlobalRecordAssociation implements Serializable {
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
     * 字符串结构格式
     */
    @javax.persistence.Column(name = "data_format", nullable = true)
    @io.swagger.annotations.ApiModelProperty("字符串结构格式")
    @org.teasoft.bee.osql.annotation.Column("data_format")
    private Long dataFormat;

    /**
     * 字符串长度
     */
    @javax.persistence.Column(name = "content_length", nullable = false)
    @io.swagger.annotations.ApiModelProperty("字符串长度")
    @org.teasoft.bee.osql.annotation.Column("content_length")
    private Short contentLength;

    /**
     * 字符串内容，最大长度为 768 个字符
     */
    @javax.persistence.Column(name = "content", nullable = false, length = 768)
    @io.swagger.annotations.ApiModelProperty("字符串内容，最大长度为 768 个字符")
    @org.teasoft.bee.osql.annotation.Column("content")
    private String content;

    private static final long serialVersionUID = 1L;

    @lombok.Getter
    @lombok.Setter
    @org.teasoft.bee.osql.annotation.JoinTable(mainField = "global_id", subField = "id", subClazz = GlobalDataRecord.class, joinType = org.teasoft.bee.osql.annotation.JoinType.LEFT_JOIN)
    private List<GlobalDataRecord> globalDataRecords;
}
