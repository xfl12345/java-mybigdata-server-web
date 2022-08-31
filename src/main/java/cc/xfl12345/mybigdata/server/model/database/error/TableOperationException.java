package cc.xfl12345.mybigdata.server.model.database.error;

import cc.xfl12345.mybigdata.server.appconst.CURD;

public class TableOperationException extends RuntimeException {
    public TableOperationException() {
    }

    public TableOperationException(String message) {
        super(message);
    }

    public TableOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TableOperationException(Throwable cause) {
        super(cause);
    }

    public TableOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    protected CURD operation;

    protected String tableName;

    protected long affectedRowsCount;

    // public TableOperationException(String message, CURD operation, String tableName) {
    //     super(message);
    //     this.operation = operation;
    //     this.tableName = tableName;
    // }

    public TableOperationException(String message, long affectedRowsCount, CURD operation, String tableName) {
        super(message);
        this.affectedRowsCount = affectedRowsCount;
        this.operation = operation;
        this.tableName = tableName;
    }

    public CURD getOperation() {
        return operation;
    }

    public String getTableName() {
        return tableName;
    }
}
