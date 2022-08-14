package cc.xfl12345.mybigdata.server.model.transaction.impl;

import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import lombok.Getter;
import lombok.Setter;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.bee.osql.transaction.Transaction;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;
import org.teasoft.honey.osql.core.SessionFactory;

public class BeeOrmTransaction
    implements cc.xfl12345.mybigdata.server.model.transaction.Transaction {
    private final Transaction transaction;

    @Getter
    @Setter
    private TransactionParam transactionParam = null;

    public BeeOrmTransaction() {
        transaction = SessionFactory.getTransaction();
    }

    private void init() throws Exception {
        if (transactionParam != null) {
            Boolean readOnly = transactionParam.getReadOnly();
            Integer timeout = transactionParam.getTimeout();
            Integer transactionIsolationLevel = transactionParam.getTransactionIsolationLevel();

            if (readOnly != null) {
                transaction.setReadOnly(readOnly);
            }
            if (timeout != null) {
                transaction.setTimeout(timeout);
            }
            if (transactionIsolationLevel != null) {
                for (TransactionIsolationLevel level : TransactionIsolationLevel.values()) {
                    if (level.getLevel() == transactionIsolationLevel) {
                        transaction.setTransactionIsolation(level);
                    }
                }
            }
        }
    }


    @Override
    public void begin() throws Exception {
        transaction.begin();
        init();
    }

    @Override
    public GlobalDataRecord lockGlobalDataRecord(GlobalDataRecord globalDataRecord) {
        Condition condition = new ConditionImpl().forUpdate();
        SuidRich suidRich = BeeFactory.getHoneyFactory().getSuidRich();
        return suidRich.select(globalDataRecord, condition).get(0);
    }

    @Override
    public void commit() {
        transaction.commit();
    }

    @Override
    public void rollback() {
        transaction.rollback();
    }

    @Override
    public int getTransactionIsolationLevel() {
        return transaction.getTransactionIsolation();
    }

    @Override
    public boolean isReadOnly() {
        return transaction.isReadOnly();
    }
}
