package cc.xfl12345.mybigdata.server.service;


import cc.xfl12345.mybigdata.server.appconst.CommonConst;
import cc.xfl12345.mybigdata.server.appconst.api.result.JsonApiResult;
import cc.xfl12345.mybigdata.server.appconst.api.result.LoginApiResult;
import cc.xfl12345.mybigdata.server.appconst.api.result.LogoutApiResult;
import cc.xfl12345.mybigdata.server.appconst.field.AccountField;
import cc.xfl12345.mybigdata.server.model.api.response.JsonCommonApiResponseObject;
import cc.xfl12345.mybigdata.server.model.checker.RegisterFieldChecker;
import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorAnalyst;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.AuthAccountMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.AuthAccount;
import cc.xfl12345.mybigdata.server.model.generator.RandomCodeGenerator;
import cc.xfl12345.mybigdata.server.utility.MyStrIsOK;
import cn.dev33.satoken.stp.StpUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * (AuthAccount)表服务实现类
 *
 * @author makejava
 * @since 2021-04-19 16:00:51
 */
@Slf4j
@Service("accountService")
public class AccountService implements InitializingBean {
    @Getter
    protected SqlErrorAnalyst sqlErrorAnalyst;

    @Autowired
    public void setSqlErrorAnalyst(SqlErrorAnalyst sqlErrorAnalyst) {
        this.sqlErrorAnalyst = sqlErrorAnalyst;
    }

    @Getter
    protected RandomCodeGenerator randomCodeGenerator;

    @Autowired
    public void setRandomCodeGenerator(RandomCodeGenerator randomCodeGenerator) {
        this.randomCodeGenerator = randomCodeGenerator;
    }

    @Getter
    protected AuthAccountMapper authAccountMapper;

    @Autowired
    public void setAuthAccountMapper(AuthAccountMapper authAccountMapper) {
        this.authAccountMapper = authAccountMapper;
    }

    @Getter
    @Setter
    protected String jsonApiVersion = "1";


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * 检查当前会话是否已登录
     *
     * @return 是否已登录
     */
    public boolean checkIsLoggedIn() {
        return StpUtil.isLogin();
    }

    public AuthAccount queryByUsername(String username) {
        // 暂不支持
        return null;
    }


    public LoginApiResult login(String username, String passwordHash) {
        LoginApiResult loginApiResult = LoginApiResult.OTHER_FAILED;
        //检查两者 是否 非空且合法
        if (RegisterFieldChecker.isUsernameUnderLegal(username) &&
            MyStrIsOK.isLetterDigitOnly(passwordHash) &&
            username.length() <= AccountField.USERNAME_MAX_LENGTH &&
            passwordHash.length() == CommonConst.SHA_512_HEX_STR_LENGTH) {
            //从数据库拉取用户信息
            AuthAccount account = queryByUsername(username);
            //如果用户不存在，则立即返回
            if (account == null) {
                loginApiResult = LoginApiResult.FAILED;
            } else {
                if (validate(account, passwordHash)) {//密码正确
                    StpUtil.logout(); // 切换账号
                    StpUtil.login(account.getAccountId());
                    loginApiResult = LoginApiResult.SUCCEED;
                }
            }
        } else {
            loginApiResult = LoginApiResult.FAILED;
        }

        return loginApiResult;
    }


    /**
     * 验证用户名和密码是否正确。
     *
     * @param account      账号数据
     * @param passwordHash 来自客户端发送来的密码SHA512 HEX值
     * @return 是否通过验证
     */
    public boolean validate(AuthAccount account, String passwordHash) {
        //默认前端已对密码文本已完成SHA512哈希值计算。这行补全完整的单向加密。这样，哪怕被拖库，也可以保证密码安全。
        String passwordHashFromRequest = passwordHashEncrypt(passwordHash, account.getPasswordSalt());
        String passwordHashFromDatabase = account.getPasswordHash();
        boolean passwordCorrect = false;
        try {
            passwordCorrect = true;
            //对比密码哈希值是否一致，使用 时间定长 的比较方法，防止试探性攻击。
            for (int i = 0; i < passwordHashFromDatabase.length(); i++) {
                if ((passwordHashFromRequest.charAt(i) ^ passwordHashFromDatabase.charAt(i)) != 0) {
                    passwordCorrect = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据库密码格式错误，可能长度不一致。e=" + e.getMessage());
            return false;
        }
        return true;
    }


    /**
     * 踢人下线，根据账号id 和 设备类型
     * 当对方再次访问系统时，会抛出NotLoginException异常，场景值=-5
     *
     * @param loginId – 账号id
     * @param device  – 设备类型 (填null代表踢出所有设备类型)
     * @return 是否成功
     */
    public LogoutApiResult kickout(Long loginId, String device) {
        StpUtil.kickout(loginId, device);
        return LogoutApiResult.SUCCEED;
    }


    public LogoutApiResult logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
            return LogoutApiResult.SUCCEED;
        } else {
            return LogoutApiResult.NO_LOGIN;
        }
    }


    public JsonCommonApiResponseObject resetPassword(String passwordHash) {
        JsonCommonApiResponseObject responseObject = new JsonCommonApiResponseObject(
            getJsonApiVersion()
        );
        Long accountId = (Long) StpUtil.getLoginId();
        if (accountId == null) {
            responseObject.setApiResult(JsonApiResult.FAILED);
        } else {
            String passwordSalt = generatePasswordSalt();
            String encryptedPasswordHash = passwordHashEncrypt(passwordHash, passwordSalt);

            AuthAccount account = new AuthAccount();
            account.setAccountId(accountId);
            account.setPasswordSalt(passwordSalt);
            account.setPasswordHash(encryptedPasswordHash);
            // TODO 事务操作
            try {
                authAccountMapper.updateById(account, accountId);
                responseObject.setApiResult(JsonApiResult.SUCCEED);
            } catch (Exception e) {
                // TODO 异常转HTTP响应文本
            }
        }

        return responseObject;
    }


    // /**
    //  * 注册账号
    //  *
    //  * @param registerInfo 注册信息
    //  * @return 返回注册结果
    //  */
    // public RegisterResult register(JSONObject registerInfo) {
    //     RegisterResult registerResult = new RegisterResult();
    //     RegisterApiResult registerApiResult = RegisterApiResult.OTHER_FAILED;
    //     String passwordStr = (String) registerInfo.get(RegisterRequestField.PASSWORD);//获取密码字段
    //     if (RegisterFieldChecker.isPasswordComplexityEnough(passwordStr)) {
    //         String passwordSalt = generatePasswordSalt();
    //         String passwordHash = generatePasswordHash(passwordStr, passwordSalt);
    //         // TODO 实现账号注册功能
    //
    //     } else {
    //         registerApiResult = RegisterApiResult.ILLEGAL_PASSWORD;
    //     }
    //     registerResult.apiResult = registerApiResult;
    //     return registerResult;
    // }

    public String passwordHashEncrypt(String passwordHash, String salt) {
        return DigestUtils.sha512Hex(passwordHash + salt);
    }

    public String generatePasswordHash(String passwordStr, String salt) {
        return passwordHashEncrypt(DigestUtils.sha512Hex(passwordStr), salt);
    }

    public String generatePasswordSalt() {
        return randomCodeGenerator.generate(AccountField.PASSWORD_SALT_LENGTH);
    }


    // /**
    //  * 通过电子邮箱查询单条数据
    //  *
    //  * @param email 电子邮箱
    //  * @return 实例对象
    //  */
    // public AuthAccount userQueryByEmail(String email) {
    //     return this.tbAccountDao.userQueryByEmail(email);
    // }
    //
    // /**
    //  * 按账号ID注销登录
    //  *
    //  * @param accountId 账号ID
    //  * @return 是否成功
    //  */
    // public boolean logoutByAccountId(Long accountId) {
    //     return this.accountSessionDao.deleteByAccountId(accountId) == 1;
    // }
    //
    // /**
    //  * 按会话ID注销登录
    //  *
    //  * @param sessionId 会话ID
    //  * @return 是否成功
    //  */
    // public boolean logoutBySessionId(String sessionId) {
    //     return this.accountSessionDao.deleteBySessionId(sessionId) == 1;
    // }
    //
    // /**
    //  * 通过用户名查询单条数据
    //  *
    //  * @param username 用户名
    //  * @return 实例对象
    //  */
    // public AuthAccount queryByUsername(String username) {
    //     return this.tbAccountDao.queryByUsername(username);
    // }
    //
    // /**
    //  * 根据用户名查询允许提供给普通用户的单条数据
    //  *
    //  * @param username 用户名
    //  * @return 实例对象
    //  */
    // AuthAccount userQueryByUsername(String username) {
    //     return this.tbAccountDao.userQueryByUsername(username);
    // }
    //
    // /**
    //  * 通过用户名查询登录验证所需要的数据
    //  *
    //  * @param username 用户名
    //  * @return 实例对象
    //  */
    // AuthAccount queryValidationInformationByUsername(String username) {
    //     return this.tbAccountDao.queryValidationInformationByUsername(username);
    // }


}
