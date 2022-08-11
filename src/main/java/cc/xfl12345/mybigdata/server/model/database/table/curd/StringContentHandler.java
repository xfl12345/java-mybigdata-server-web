package cc.xfl12345.mybigdata.server.model.database.table.curd;

import cc.xfl12345.mybigdata.server.model.database.table.curd.base.AppTableCurdHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;

import java.util.List;

public interface StringContentHandler extends AppTableCurdHandler<StringContent> {
    List<StringContent> selectByPrefix(String prefix, String[] fields);
}




