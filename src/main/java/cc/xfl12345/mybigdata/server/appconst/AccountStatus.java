package cc.xfl12345.mybigdata.server.appconst;

public enum AccountStatus {
    FROZEN(0),
    NORMAL(1),
    EMAIL_NOT_ACTIVATED(2),
    EMAIL_INVALID(3);

    AccountStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    private final int statusCode;

    public int getStatusCode() {
        return statusCode;
    }
}
