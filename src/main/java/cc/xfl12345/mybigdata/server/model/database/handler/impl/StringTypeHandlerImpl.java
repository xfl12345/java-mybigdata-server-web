package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.model.database.error.TableDataException;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.table.constant.StringContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.StringContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.Op;
import org.teasoft.honey.osql.core.ConditionImpl;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StringTypeHandlerImpl extends BaseDataHandler implements StringTypeHandler {
    @Getter
    protected StringContentHandler stringContentHandler;

    @Autowired
    public void setStringContentHandler(StringContentHandler stringContentHandler) {
        this.stringContentHandler = stringContentHandler;
    }

    protected String[] selectContentFieldOnly;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (stringContentHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("stringContentHandler"));
        }

        selectContentFieldOnly = new String[] {StringContentConstant.CONTENT};
    }

    protected StringContent getPojo(String content) throws TableDataException {
        StringContent stringContent = new StringContent();
        stringContent.setContent(content);

        int lenOfStr = content.length();
        if (lenOfStr > Short.MAX_VALUE) {
            throw new TableDataException("String content is too long!");
        }
        stringContent.setContentLength((short) lenOfStr);
        return stringContent;
    }


    @Override
    public Long insertAndReturnId(String value) throws Exception {
        return stringContentHandler.insertAndReturnId(getPojo(value));
    }

    @Override
    public long insert(String value) throws Exception {
        return stringContentHandler.insert(getPojo(value));
    }

    @Override
    public long insertBatch(List<String> values) throws Exception {
        ArrayList<StringContent> stringContents = new ArrayList<>((values.size() * 3) >> 1);
        for (String value : values) {
            stringContents.add(getPojo(value));
        }

        return stringContentHandler.insertBatch(stringContents);
    }

    @Override
    public Long selectId(String value) throws Exception {
        return stringContentHandler.selectId(getPojo(value));
    }

    @Override
    public String selectById(Long globalId) throws Exception {
        return stringContentHandler.selectById(globalId, selectContentFieldOnly).getContent();
    }

    @Override
    public Long insertOrSelect4Id(String value) throws Exception {
        return stringContentHandler.insertOrSelect4Id(getPojo(value));
    }

    @Override
    public void updateById(String value, Long globalId) throws Exception {
        stringContentHandler.updateById(getPojo(value), globalId);
    }

    @Override
    public void deleteById(Long globalId) throws Exception {
        stringContentHandler.deleteById(globalId);
    }


    public void updateStringByFullText(String oldValue, String value) throws Exception {
        Condition condition = new ConditionImpl();
        condition.forUpdate().op(StringContentConstant.DB_CONTENT, Op.eq, oldValue);
        stringContentHandler.update(getPojo(value), condition);
    }

    @Override
    public List<String> selectStringByPrefix(String prefix) {
        List<StringContent> stringContents = stringContentHandler.selectByPrefix(prefix, selectContentFieldOnly);
        return stringContents.parallelStream().map(StringContent::getContent).toList();
    }
}
