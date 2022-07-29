package cc.xfl12345.mybigdata.server.model.database.association;

import java.io.Serializable;
import java.util.List;

/**
 * 表名：object_record
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "object_record")
@org.teasoft.bee.osql.annotation.Table("object_record")
public class ObjectRecordGlobalRecordAssociation implements Serializable {
    /**
     * 对象id
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    @io.swagger.annotations.ApiModelProperty("对象id")
    @javax.persistence.Id
    @org.teasoft.bee.osql.annotation.Column("global_id")
    @org.teasoft.bee.osql.annotation.PrimaryKey
    private Long globalId;

    /**
     * 用于检验该对象的JSON Schema
     */
    @javax.persistence.Column(name = "object_schema", nullable = true)
    @io.swagger.annotations.ApiModelProperty("用于检验该对象的JSON Schema")
    @org.teasoft.bee.osql.annotation.Column("object_schema")
    private Long objectSchema;

    /**
     * 对象名称
     */
    @javax.persistence.Column(name = "object_name", nullable = false)
    @io.swagger.annotations.ApiModelProperty("对象名称")
    @org.teasoft.bee.osql.annotation.Column("object_name")
    private Long objectName;

    private static final long serialVersionUID = 1L;

    @lombok.Getter
    @lombok.Setter
    @org.teasoft.bee.osql.annotation.JoinTable(mainField = "global_id", subField = "id", subClazz = cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord.class, joinType = org.teasoft.bee.osql.annotation.JoinType.LEFT_JOIN)
    private List<cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord> globalDataRecords;
}
