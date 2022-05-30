package cc.xfl12345.mybigdata.server.model.database.result;

import cc.xfl12345.mybigdata.server.model.database.table.IntegerContent;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class NumberTypeResult extends SingleDataResultBase {
    @Getter
    @Setter
    protected BigDecimal number = null;

    @Getter
    protected IntegerContent integerContent = null;

    public void setIntegerContent(IntegerContent integerContent) {
        this.integerContent = integerContent;
        number = new BigDecimal(integerContent.getContent());
    }

    @Getter
    protected StringContent stringContent = null;

    public void setStringContent(StringContent stringContent) {
        this.stringContent = stringContent;
        number = new BigDecimal(stringContent.getContent());
    }

    public Double getDouble() {
        return number.doubleValue();
    }

    public Long getLong() {
        return number.longValue();
    }

    public String getString() {
        return number.toPlainString();
    }
}
