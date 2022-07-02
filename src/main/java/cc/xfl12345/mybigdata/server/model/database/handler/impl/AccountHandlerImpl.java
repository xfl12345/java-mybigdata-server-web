package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.model.database.handler.AccountHandler;
import cc.xfl12345.mybigdata.server.model.database.result.BooleanTypeResult;
import cc.xfl12345.mybigdata.server.model.database.result.NumberTypeResult;
import cc.xfl12345.mybigdata.server.model.database.table.AuthAccount;
import lombok.extern.slf4j.Slf4j;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.bee.osql.transaction.Transaction;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.HoneyFactory;
import org.teasoft.honey.osql.core.SessionFactory;

import java.util.List;

@Slf4j
public class AccountHandlerImpl extends AbstractTableHandler implements AccountHandler {

    //TODO  丰富账号的信息记录与功能，包括创建账号的时间
    @Override
    public NumberTypeResult insertAccount(AuthAccount account) {
        NumberTypeResult result = new NumberTypeResult();

        // 开启事务
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
            SuidRich suid = honeyFactory.getSuidRich();

            // 插入数据
            long accountId = suid.insertAndReturnId(account);
            transaction.commit();
            result.setSimpleResult(SimpleCoreTableCurdResult.SUCCEED);
            result.setNumber(accountId);
        } catch (BeeException | NullPointerException e) {
            sqlErrorHandler.defaultErrorHandler(e, transaction, result);
        }

        return result;
    }

    protected List<AuthAccount> selectAccount(Condition condition, boolean onlyOne) {
        List<AuthAccount> result = null;
        // 开启事务
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
            SuidRich suid = honeyFactory.getSuidRich();

            // 查询数据
            result = suid.select(new AuthAccount(), condition);
            if (onlyOne) {
                if (result.size() == 1) {
                    transaction.commit();
                } else {
                    transaction.rollback();
                    result = null;
                }
            } else {
                transaction.commit();
            }
        } catch (Exception e) {
            transaction.rollback();
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public AuthAccount selectOneAccount(Condition condition) {
        List<AuthAccount> result = selectAccount(condition, true);
        return result != null && result.size() == 1 ? result.get(0) : null;
    }

    @Override
    public List<AuthAccount> selectAccount(Condition condition) {
        return selectAccount(condition, false);
    }

    @Override
    public BooleanTypeResult updateOneAccount(AuthAccount account, Condition condition) {
        return null;
    }

    @Override
    public NumberTypeResult updateAccount(AuthAccount account, Condition condition) {
        return null;
    }

    @Override
    public BooleanTypeResult deleteOneAccount(AuthAccount account, Condition condition) {
        return null;
    }

    @Override
    public NumberTypeResult deleteAccount(AuthAccount account, Condition condition) {
        return null;
    }


}
