package cc.xfl12345.mybigdata.server.model.database.association;

import java.io.Serializable;
import java.util.List;

/**
 * 表名：group_record
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "group_record")
@org.teasoft.bee.osql.annotation.Table("group_record")
public class GroupRecordAssociation implements Serializable {
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
     * 组名
     */
    @javax.persistence.Column(name = "group_name", nullable = false)
    @io.swagger.annotations.ApiModelProperty("组名")
    @org.teasoft.bee.osql.annotation.Column("group_name")
    private Long groupName;

    private static final long serialVersionUID = 1L;

    @lombok.Getter
    @lombok.Setter
    @org.teasoft.bee.osql.annotation.JoinTable(mainField = "global_id", subField = "id", subClazz = cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord.class, joinType = org.teasoft.bee.osql.annotation.JoinType.LEFT_JOIN)
    private List<cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord> globalDataRecords;
}
