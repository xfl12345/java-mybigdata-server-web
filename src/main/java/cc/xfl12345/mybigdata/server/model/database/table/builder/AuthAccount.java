package cc.xfl12345.mybigdata.server.model.database.table.builder;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 表名：auth_account
*/
@ToString
@SuperBuilder
@Table(name = "`auth_account`")
public class AuthAccount implements Serializable {
    /**
     * 账号ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 账号密码的哈希值
     */
    @Column(name = "`password_hash`")
    private String passwordHash;

    /**
     * 账号密码的哈希值计算的佐料
     */
    @Column(name = "`password_salt`")
    private String passwordSalt;

    /**
     * 账号额外信息
     */
    @Column(name = "`extra_info_id`")
    private Long extraInfoId;

    private static final long serialVersionUID = 1L;

    /**
     * 获取账号ID
     *
     * @return id - 账号ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置账号ID
     *
     * @param id 账号ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取账号密码的哈希值
     *
     * @return passwordHash - 账号密码的哈希值
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * 设置账号密码的哈希值
     *
     * @param passwordHash 账号密码的哈希值
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * 获取账号密码的哈希值计算的佐料
     *
     * @return passwordSalt - 账号密码的哈希值计算的佐料
     */
    public String getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * 设置账号密码的哈希值计算的佐料
     *
     * @param passwordSalt 账号密码的哈希值计算的佐料
     */
    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    /**
     * 获取账号额外信息
     *
     * @return extraInfoId - 账号额外信息
     */
    public Long getExtraInfoId() {
        return extraInfoId;
    }

    /**
     * 设置账号额外信息
     *
     * @param extraInfoId 账号额外信息
     */
    public void setExtraInfoId(Long extraInfoId) {
        this.extraInfoId = extraInfoId;
    }
}