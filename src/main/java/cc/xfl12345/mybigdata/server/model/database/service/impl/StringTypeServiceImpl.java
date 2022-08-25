package cc.xfl12345.mybigdata.server.model.database.service.impl;

import cc.xfl12345.mybigdata.server.model.database.service.StringTypeService;
import cc.xfl12345.mybigdata.server.model.database.table.constant.StringContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.StringContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.AppTableCurdMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import lombok.Setter;

public class StringTypeServiceImpl extends AppSingleTableDataService<String, StringContent> implements StringTypeService {
    @Setter
    protected StringContentMapper mapper;

    @Override
    public AppTableCurdMapper<StringContent> getMapper() {
        return mapper;
    }

    protected String[] selectContentFieldOnly = new String[]{StringContentConstant.CONTENT};

    @Override
    protected String[] getSelectContentFieldOnly() {
        return selectContentFieldOnly;
    }

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
}
