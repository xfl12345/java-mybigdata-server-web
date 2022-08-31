package cc.xfl12345.mybigdata.server.model.database.error;

public class TableDataException extends RuntimeException {
    public TableDataException() {
    }

    public TableDataException(String message) {
        super(message);
    }

    public TableDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public TableDataException(Throwable cause) {
        super(cause);
    }

    public TableDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected String tableName;

    protected String[] uuids;

    public TableDataException(String message, String tableName) {
        super(message);
        this.tableName = tableName;
    }

    public TableDataException(String message, String tableName, String[] uuids) {
        super(message);
        this.tableName = tableName;
        this.uuids = uuids;
    }

    public String[] getUuids() {
        return uuids;
    }

    public String getTableName() {
        return tableName;
    }
}
