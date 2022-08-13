package cc.xfl12345.mybigdata.server.model.transaction.impl;

import lombok.Getter;
import lombok.Setter;

public class TransactionParam {
    @Getter
    @Setter
    private Boolean readOnly;

    @Getter
    @Setter
    private Integer timeout;

    @Getter
    @Setter
    private Integer transactionIsolationLevel;

    public static final class TransactionParamBuilder {
        private Boolean readOnly;
        private Integer timeout;
        private Integer transactionIsolationLevel;

        private TransactionParamBuilder() {
        }

        public static TransactionParamBuilder aTransactionParam() {
            return new TransactionParamBuilder();
        }

        public TransactionParamBuilder withReadOnly(Boolean readOnly) {
            this.readOnly = readOnly;
            return this;
        }

        public TransactionParamBuilder withTimeout(Integer timeout) {
            this.timeout = timeout;
            return this;
        }

        public TransactionParamBuilder withTransactionIsolationLevel(Integer transactionIsolationLevel) {
            this.transactionIsolationLevel = transactionIsolationLevel;
            return this;
        }

        public TransactionParam build() {
            TransactionParam transactionParam = new TransactionParam();
            transactionParam.setReadOnly(readOnly);
            transactionParam.setTimeout(timeout);
            transactionParam.setTransactionIsolationLevel(transactionIsolationLevel);
            return transactionParam;
        }
    }
}
