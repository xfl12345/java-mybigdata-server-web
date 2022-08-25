package cc.xfl12345.mybigdata.server.model.data.handler;

import cc.xfl12345.mybigdata.server.model.database.table.pojo.NumberContent;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class NumberTypeHandler extends SingleTableDataHandler<BigDecimal, NumberContent> {
    protected boolean isInteger(BigDecimal value) {
        return value.scale() <= 0;
    }

    @Override
    protected NumberContent getPojo(BigDecimal value) {
        NumberContent numberContent = new NumberContent();
        String numberInString = value.toPlainString();

        numberContent.setNumberisinteger(isInteger(value));
        numberContent.setNumberis64bit(
            numberContent.getNumberisinteger() && new BigDecimal(value.longValue()).compareTo(value) == 0
        );
        numberContent.setContent(numberInString);

        return numberContent;
    }

    @Override
    protected BigDecimal getValue(NumberContent pojo) {
        return new BigDecimal(pojo.getContent());
    }
}
