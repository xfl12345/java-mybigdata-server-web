package cc.xfl12345.mybigdata.server.appconst.field;

public class AccountField {
    public static final long ROOT_ACCOUNT_ID = 1;
    public static final String ROOT_USERNAME = "root";
    public static final String ROOT_ACCOUNT_INIT_PASSWORD = "mybigdata";

    public static final int USERNAME_MIN_LENGTH = 6;
    public static final int USERNAME_MAX_LENGTH = 32;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 30;
    public static final int GENDER_LENGTH = 1;
    public static final int EMAIL_MAX_LENGTH = 255;

    public static final int PASSWORD_HASH_LENGTH = 128;
    public static final int PASSWORD_SALT_LENGTH = 128;
}
