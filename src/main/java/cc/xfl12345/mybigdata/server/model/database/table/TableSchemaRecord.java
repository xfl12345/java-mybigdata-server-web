package cc.xfl12345.mybigdata.server.model.database.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 表名：table_schema_record
*/
@Data
@ApiModel("")
@Table(name = "table_schema_record")
@Entity
@org.teasoft.bee.osql.annotation.Table("table_schema_record")
public class TableSchemaRecord implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Column(name = "global_id")
    @ApiModelProperty("当前表所在数据库实例里的全局ID")
    @Id
    @org.teasoft.bee.osql.annotation.PrimaryKey
    private Long globalId;

    /**
     * 插表模型名称
     */
    @Column(name = "schema_name")
    @ApiModelProperty("插表模型名称")
    private Long schemaName;

    /**
     * json_schema 字段的长度
     */
    @Column(name = "content_length")
    @ApiModelProperty("json_schema 字段的长度")
    private Short contentLength;

    /**
     * 插表模型
     */
    @Column(name = "json_schema")
    @ApiModelProperty("插表模型")
    private String jsonSchema;

    private static final long serialVersionUID = 1L;
}
