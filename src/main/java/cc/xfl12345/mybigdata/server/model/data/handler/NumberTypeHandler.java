package cc.xfl12345.mybigdata.server.model.data.handler;

import cc.xfl12345.mybigdata.server.model.database.table.constant.NumberContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.NumberContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.NumberContent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class NumberTypeHandler extends SingleTableDataHandler<Long, BigDecimal, NumberContent> {
    @Getter
    protected NumberContentHandler numberContentHandler = null;

    public void setNumberContentHandler(NumberContentHandler numberContentHandler) {
        this.numberContentHandler = numberContentHandler;
    }

    protected String[] selectContentFieldOnly;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (numberContentHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("numberTypeHandler"));
        }

        selectContentFieldOnly = new String[] {NumberContentConstant.CONTENT};
    }

    protected boolean isInteger(BigDecimal value) {
        return value.scale() <= 0;
    }

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
    protected BigDecimal getValue(NumberContent pojo) throws Exception {
        return new BigDecimal(pojo.getContent());
    }
}
