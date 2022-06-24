package cc.xfl12345.mybigdata.server.appconst.field;

public class AccountField {

    public static final class ACCOUNT_STATUS {
        public static final Integer FROZEN = 0;
        public static final Integer NORMAL = 1;
        public static final Integer EMAIL_INVALID = 3;
        public static final Integer EMAIL_NOT_ACTIVATED = 2;
    }

    public static final int USERNAME_MIN_LENGTH = 6;
    public static final int USERNAME_MAX_LENGTH = 32;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 30;
    public static final int GENDER_LENGTH = 1;
    public static final int EMAIL_MAX_LENGTH = 255;

    public static final int PASSWORD_HASH_LENGTH = 128;
    public static final int PASSWORD_SALT_LENGTH = 128;

}
