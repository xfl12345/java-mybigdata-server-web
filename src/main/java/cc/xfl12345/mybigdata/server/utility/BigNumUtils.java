package cc.xfl12345.mybigdata.server.utility;

import java.math.BigDecimal;

public class BigNumUtils {
    public static BigDecimal toUnsignedLong(long value) {
        if (value >= 0)
            return new BigDecimal(value);
        long lowValue = value & Long.MAX_VALUE;
        return BigDecimal.valueOf(lowValue).add(BigDecimal.valueOf(Long.MAX_VALUE)).add(BigDecimal.valueOf(1));
    }
}
