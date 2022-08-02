package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.handler.AccountHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.NumberTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.AuthAccount;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.teasoft.bee.osql.Condition;

import java.util.List;

import static cc.xfl12345.mybigdata.server.utility.BeeOrmUtils.getSuidRich;

@Slf4j
public class AccountHandlerImpl extends AbstractCoreTableHandler implements AccountHandler {

    @Getter
    @Setter
    protected NumberTypeHandler numberTypeHandler;

    @Override
    public Long insertAccount(AuthAccount account) {
        return getSuidRich().insertAndReturnId(account);
    }

    @Override
    public AuthAccount selectOneAccount(Condition condition) throws TableOperationException {
        List<AuthAccount> result = selectAccount(condition);
        int affectedRowCount = result.size();
        if (affectedRowCount == 1) {
            return result.get(0);
        } else if (affectedRowCount == 0) {
            return null;
        } else {
            throw getAffectedRowShouldBe1Exception(
                affectedRowCount,
                CURD.RETRIEVE,
                CoreTableNames.AUTH_ACCOUNT.getName()
            );
        }
    }

    @Override
    public List<AuthAccount> selectAccount(Condition condition) {
        return getSuidRich().select(new AuthAccount(), condition);
    }

    @Override
    public boolean updateOneAccount(AuthAccount account, Condition condition) throws TableOperationException {
        int affectedRowCount = updateAccount(account, condition);
        if (affectedRowCount == 1) {
            return true;
        } else if (affectedRowCount == 0) {
            return false;
        } else {
            throw getUpdateShouldBe1Exception(
                affectedRowCount,
                CoreTableNames.AUTH_ACCOUNT.getName()
            );
        }
    }

    @Override
    public int updateAccount(AuthAccount account, Condition condition) {
        return getSuidRich().update(account, condition);
    }

    @Override
    public boolean deleteOneAccount(AuthAccount account, Condition condition) throws TableOperationException {
        int affectedRowCount = deleteAccount(account, condition);
        if (affectedRowCount == 1) {
            return true;
        } else if (affectedRowCount == 0) {
            return false;
        } else {
            throw getAffectedRowShouldBe1Exception(
                affectedRowCount,
                CURD.DELETE,
                CoreTableNames.AUTH_ACCOUNT.getName()
            );
        }
    }

    @Override
    public int deleteAccount(AuthAccount account, Condition condition) {
        return getSuidRich().delete(account, condition);
    }


}
