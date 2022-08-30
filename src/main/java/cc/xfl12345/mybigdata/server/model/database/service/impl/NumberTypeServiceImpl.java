package cc.xfl12345.mybigdata.server.model.database.service.impl;

import cc.xfl12345.mybigdata.server.model.database.service.NumberTypeService;
import cc.xfl12345.mybigdata.server.model.database.table.constant.NumberContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.NumberContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.AppTableCurdMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.NumberContent;
import lombok.Setter;

import java.math.BigDecimal;

public class NumberTypeServiceImpl extends SingleTableDataService<BigDecimal, NumberContent> implements NumberTypeService {
    @Setter
    protected NumberContentMapper mapper;

    @Override
    public AppTableCurdMapper<NumberContent> getMapper() {
        return mapper;
    }

    protected String[] selectContentFieldOnly = new String[]{NumberContentConstant.CONTENT};

    @Override
    protected String[] getSelectContentFieldOnly() {
        return selectContentFieldOnly;
    }

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
