package cc.xfl12345.mybigdata.server.model.data.handler;

import cc.xfl12345.mybigdata.server.model.database.error.TableDataException;
import cc.xfl12345.mybigdata.server.model.database.table.constant.StringContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.StringContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class StringTypeHandler extends SingleTableDataHandler<Long, String, StringContent> {
    protected String[] selectContentFieldOnly;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (dataHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("dataHandler"));
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
    protected String getValue(StringContent pojo) throws Exception {
        return pojo.getContent();
    }
}
