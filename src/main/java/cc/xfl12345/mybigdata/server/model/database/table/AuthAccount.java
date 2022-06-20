package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;

/**
 * 表名：auth_account
*/
@lombok.Data
@io.swagger.annotations.ApiModel("")
@javax.persistence.Table(name = "auth_account")
@javax.persistence.Entity
@org.teasoft.bee.osql.annotation.Table("auth_account")
public class AuthAccount implements Cloneable, Serializable {
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

    @Override
    public AuthAccount clone() throws CloneNotSupportedException {
        return (AuthAccount) super.clone();
    }
}
