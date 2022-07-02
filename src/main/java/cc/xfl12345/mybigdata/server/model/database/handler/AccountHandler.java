package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.BooleanTypeResult;
import cc.xfl12345.mybigdata.server.model.database.result.NumberTypeResult;
import cc.xfl12345.mybigdata.server.model.database.table.AuthAccount;
import org.teasoft.bee.osql.Condition;

import java.util.List;

public interface AccountHandler {
    NumberTypeResult insertAccount(AuthAccount account);

    AuthAccount selectOneAccount(Condition condition);

    List<AuthAccount> selectAccount(Condition condition);

    BooleanTypeResult updateOneAccount(AuthAccount account, Condition condition);

    NumberTypeResult updateAccount(AuthAccount account, Condition condition);

    BooleanTypeResult deleteOneAccount(AuthAccount account, Condition condition);

    NumberTypeResult deleteAccount(AuthAccount account, Condition condition);
}
