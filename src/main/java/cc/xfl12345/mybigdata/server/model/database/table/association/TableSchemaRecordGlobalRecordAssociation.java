package cc.xfl12345.mybigdata.server.model.database.table.association;

import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;

import java.io.Serializable;
import java.util.List;

/**
 * 表名：table_schema_record
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "table_schema_record")
@org.teasoft.bee.osql.annotation.Table("table_schema_record")
public class TableSchemaRecordGlobalRecordAssociation implements Serializable {
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
     * 插表模型名称
     */
    @javax.persistence.Column(name = "schema_name", nullable = false)
    @io.swagger.annotations.ApiModelProperty("插表模型名称")
    @org.teasoft.bee.osql.annotation.Column("schema_name")
    private Long schemaName;

    /**
     * json_schema 字段的长度
     */
    @javax.persistence.Column(name = "content_length", nullable = false)
    @io.swagger.annotations.ApiModelProperty("json_schema 字段的长度")
    @org.teasoft.bee.osql.annotation.Column("content_length")
    private Short contentLength;

    /**
     * 插表模型
     */
    @javax.persistence.Column(name = "json_schema", nullable = false, length = 16000)
    @io.swagger.annotations.ApiModelProperty("插表模型")
    @org.teasoft.bee.osql.annotation.Column("json_schema")
    private String jsonSchema;

    private static final long serialVersionUID = 1L;

    @lombok.Getter
    @lombok.Setter
    @org.teasoft.bee.osql.annotation.JoinTable(mainField = "global_id", subField = "id", subClazz = GlobalDataRecord.class, joinType = org.teasoft.bee.osql.annotation.JoinType.LEFT_JOIN)
    private List<GlobalDataRecord> globalDataRecords;
}
