package cc.xfl12345.mybigdata.server.model.transaction.impl;

import cc.xfl12345.mybigdata.server.model.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

public class TransactionFactory {
    @Getter
    @Setter
    private TransactionParam defaultTransactionParam = null;

    public Transaction getTransaction() {
        return getTransaction(this.defaultTransactionParam);
    }

    public Transaction getTransaction(TransactionParam transactionParam) {
        BeeOrmTransaction transaction = new BeeOrmTransaction();
        transaction.setTransactionParam(transactionParam);

        return transaction;
    }
}
