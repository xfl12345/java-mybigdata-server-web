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

    protected Long[] globalIds;

    protected String[] uuids;

    public TableDataException(String message, Long[] globalIds, String tableName) {
        super(message);
        this.globalIds = globalIds;
        this.tableName = tableName;
    }

    public TableDataException(String message, Long[] globalIds, String[] uuids, String tableName) {
        super(message);
        this.globalIds = globalIds;
        this.uuids = uuids;
        this.tableName = tableName;
    }

    public Long[] getGlobalIds() {
        return globalIds;
    }

    public String[] getUuids() {
        return uuids;
    }

    public String getTableName() {
        return tableName;
    }
}
