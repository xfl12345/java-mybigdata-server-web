package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.AuthAccount;
import org.teasoft.bee.osql.Condition;

import java.util.List;

public interface AccountHandler {

    /**
     * 插入账号，返回 账号ID
     */
    Long insertAccount(AuthAccount account);

    AuthAccount selectOneAccount(Condition condition) throws TableOperationException;

    List<AuthAccount> selectAccount(Condition condition);


    /**
     * 更新账号，返回 影响条数
     */
    boolean updateOneAccount(AuthAccount account, Condition condition) throws TableOperationException;

    int updateAccount(AuthAccount account, Condition condition);

    boolean deleteOneAccount(AuthAccount account, Condition condition) throws TableOperationException;

    int deleteAccount(AuthAccount account, Condition condition);
}
