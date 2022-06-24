package cc.xfl12345.mybigdata.server.appconst;

public enum Gender {
    DEFAULT('0'),
    MALE('1'),
    FEMALE('2'),
    HERMAPHRODITE('3');

    Gender(char genderCode) {
        this.genderCode = genderCode;
    }

    private final char genderCode;

    public char getGenderCode() {
        return genderCode;
    }
}
