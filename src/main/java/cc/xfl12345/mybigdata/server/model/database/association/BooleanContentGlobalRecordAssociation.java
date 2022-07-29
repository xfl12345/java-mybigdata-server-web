package cc.xfl12345.mybigdata.server.model.database.association;

import java.io.Serializable;
import java.util.List;

/**
 * 表名：boolean_content
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "boolean_content")
@org.teasoft.bee.osql.annotation.Table("boolean_content")
public class BooleanContentGlobalRecordAssociation implements Serializable {
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
     * 布尔值
     */
    @javax.persistence.Column(name = "content", nullable = false)
    @io.swagger.annotations.ApiModelProperty("布尔值")
    @org.teasoft.bee.osql.annotation.Column("content")
    private Boolean content;

    private static final long serialVersionUID = 1L;

    @lombok.Getter
    @lombok.Setter
    @org.teasoft.bee.osql.annotation.JoinTable(mainField = "global_id", subField = "id", subClazz = cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord.class, joinType = org.teasoft.bee.osql.annotation.JoinType.LEFT_JOIN)
    private List<cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord> globalDataRecords;
}
