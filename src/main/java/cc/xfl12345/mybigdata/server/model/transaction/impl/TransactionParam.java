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

    public static final class Builder {
        private Boolean readOnly;
        private Integer timeout;
        private Integer transactionIsolationLevel;

        private Builder() {
        }

        public static Builder aBuilder() {
            return new Builder();
        }

        public Builder withReadOnly(Boolean readOnly) {
            this.readOnly = readOnly;
            return this;
        }

        public Builder withTimeout(Integer timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder withTransactionIsolationLevel(Integer transactionIsolationLevel) {
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
