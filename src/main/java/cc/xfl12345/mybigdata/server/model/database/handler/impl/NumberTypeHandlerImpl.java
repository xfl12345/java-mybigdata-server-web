package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.model.database.handler.NumberTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.table.constant.NumberContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.NumberContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.NumberContent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class NumberTypeHandlerImpl extends BaseDataHandler implements NumberTypeHandler {
    @Getter
    protected NumberContentHandler numberContentHandler = null;

    @Autowired
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
    public Long insertAndReturnId(BigDecimal value) throws Exception {
        return numberContentHandler.insertAndReturnId(getPojo(value));
    }

    @Override
    public long insert(BigDecimal value) throws Exception {
        return numberContentHandler.insert(getPojo(value));
    }

    @Override
    public long insertBatch(List<BigDecimal> values) throws Exception {
        return numberContentHandler.insertBatch(values.parallelStream().map(this::getPojo).toList());
    }

    @Override
    public Long selectId(BigDecimal value) throws Exception {
        return numberContentHandler.selectId(getPojo(value));
    }

    @Override
    public BigDecimal selectById(Long globalId) throws Exception {
        return new BigDecimal(numberContentHandler.selectById(globalId, selectContentFieldOnly).getContent());
    }

    @Override
    public Long insertOrSelect4Id(BigDecimal value) throws Exception {
        return numberContentHandler.insertOrSelect4Id(getPojo(value));
    }

    @Override
    public void updateById(BigDecimal value, Long globalId) throws Exception {
        numberContentHandler.updateById(getPojo(value), globalId);
    }

    @Override
    public void deleteById(Long globalId) throws Exception {
        numberContentHandler.deleteById(globalId);
    }
}
