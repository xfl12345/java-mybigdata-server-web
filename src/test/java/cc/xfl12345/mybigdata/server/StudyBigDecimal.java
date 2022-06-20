package cc.xfl12345.mybigdata.server;

import java.math.BigDecimal;

public class StudyBigDecimal {
    public static void main(String[] args) throws Exception {
        System.out.println(isLongInteger(new BigDecimal("0")));
        System.out.println(isLongInteger(new BigDecimal("0.0")));
        System.out.println(isLongInteger(new BigDecimal("1000000000000000.000000")));
        System.out.println(isLongInteger(new BigDecimal("1000000000000000")));
        System.out.println(isLongInteger(new BigDecimal(Long.MAX_VALUE)));
        System.out.println(isLongInteger(new BigDecimal(Long.MIN_VALUE)));
        System.out.println(isLongInteger(new BigDecimal(Long.MAX_VALUE + "0")));
    }

    public static boolean isLongInteger(BigDecimal number) {
        System.out.println(number.toPlainString());
        System.out.println("number.precision()=" + number.precision());
        System.out.println("number.scale()=" + number.scale());
        System.out.println("number.signum()=" + number.signum());
        return number.scale() == 0 && new BigDecimal(number.longValue()).compareTo(number) == 0;
    }
}
