package cc.xfl12345.mybigdata.server.model.database.table.association;

import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;

import java.io.Serializable;
import java.util.List;

/**
 * 表名：group_content
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "group_content")
@org.teasoft.bee.osql.annotation.Table("group_content")
public class GroupContentGlobalRecordAssociation implements Serializable {
    /**
     * 组id
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    @io.swagger.annotations.ApiModelProperty("组id")
    @javax.persistence.Id
    @org.teasoft.bee.osql.annotation.Column("global_id")
    @org.teasoft.bee.osql.annotation.PrimaryKey
    private Long globalId;

    /**
     * 组内对象的下标
     */
    @javax.persistence.Column(name = "item_index", nullable = true)
    @io.swagger.annotations.ApiModelProperty("组内对象的下标")
    @org.teasoft.bee.osql.annotation.Column("item_index")
    private Long itemIndex;

    /**
     * 组内对象
     */
    @javax.persistence.Column(name = "item", nullable = false)
    @io.swagger.annotations.ApiModelProperty("组内对象")
    @org.teasoft.bee.osql.annotation.Column("item")
    private Long item;

    private static final long serialVersionUID = 1L;

    @lombok.Getter
    @lombok.Setter
    @org.teasoft.bee.osql.annotation.JoinTable(mainField = "global_id", subField = "id", subClazz = GlobalDataRecord.class, joinType = org.teasoft.bee.osql.annotation.JoinType.LEFT_JOIN)
    private List<GlobalDataRecord> globalDataRecords;
}
