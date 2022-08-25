package cc.xfl12345.mybigdata.server.model.database.service.impl;

import cc.xfl12345.mybigdata.server.model.database.service.StringTypeService;
import cc.xfl12345.mybigdata.server.model.database.table.constant.StringContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.StringContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.AppTableCurdMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import lombok.Getter;
import lombok.Setter;

public class StringTypeServiceImpl extends AppSingleTableDataService<String, StringContent> implements StringTypeService {
    @Getter
    @Setter
    protected StringContentMapper stringContentMapper;

    protected String[] selectContentFieldOnly = new String[]{StringContentConstant.CONTENT};

    @Override
    protected StringContent getPojo(String s) {
        StringContent stringContent = new StringContent();
        stringContent.setContent(s);
        return stringContent;
    }

    @Override
    protected String getValue(StringContent stringContent) {
        return stringContent.getContent();
    }

    @Override
    public AppTableCurdMapper<StringContent> getMapper() {
        return stringContentMapper;
    }

    @Override
    protected String[] getSelectContentFieldOnly() {
        return selectContentFieldOnly;
    }
}
