package cc.xfl12345.mybigdata.server.model.data.handler;

import cc.xfl12345.mybigdata.server.model.database.error.TableDataException;
import cc.xfl12345.mybigdata.server.model.database.table.constant.StringContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringTypeHandler extends SingleTableDataHandler<Long, String, StringContent> {
    @Override
    protected StringContent getPojo(String content) {
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
    protected String getValue(StringContent pojo) {
        return pojo.getContent();
    }
}
