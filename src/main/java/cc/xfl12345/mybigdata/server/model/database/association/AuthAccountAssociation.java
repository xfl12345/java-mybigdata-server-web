package cc.xfl12345.mybigdata.server.model.database.association;

import java.io.Serializable;
import java.util.List;

/**
 * 表名：auth_account
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "auth_account")
@org.teasoft.bee.osql.annotation.Table("auth_account")
public class AuthAccountAssociation implements Serializable {
    /**
     * 账号ID
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    @io.swagger.annotations.ApiModelProperty("账号ID")
    @javax.persistence.Id
    @org.teasoft.bee.osql.annotation.Column("global_id")
    @org.teasoft.bee.osql.annotation.PrimaryKey
    private Long globalId;

    /**
     * 账号密码的哈希值
     */
    @javax.persistence.Column(name = "password_hash", nullable = false, length = 128)
    @io.swagger.annotations.ApiModelProperty("账号密码的哈希值")
    @org.teasoft.bee.osql.annotation.Column("password_hash")
    private String passwordHash;

    /**
     * 账号密码的哈希值计算的佐料
     */
    @javax.persistence.Column(name = "password_salt", nullable = false, length = 128)
    @io.swagger.annotations.ApiModelProperty("账号密码的哈希值计算的佐料")
    @org.teasoft.bee.osql.annotation.Column("password_salt")
    private String passwordSalt;

    /**
     * 账号额外信息
     */
    @javax.persistence.Column(name = "extra_info_id", nullable = false)
    @io.swagger.annotations.ApiModelProperty("账号额外信息")
    @org.teasoft.bee.osql.annotation.Column("extra_info_id")
    private Long extraInfoId;

    private static final long serialVersionUID = 1L;

    @lombok.Getter
    @lombok.Setter
    @org.teasoft.bee.osql.annotation.JoinTable(mainField = "global_id", subField = "id", subClazz = cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord.class, joinType = org.teasoft.bee.osql.annotation.JoinType.LEFT_JOIN)
    private List<cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord> globalDataRecords;
}
