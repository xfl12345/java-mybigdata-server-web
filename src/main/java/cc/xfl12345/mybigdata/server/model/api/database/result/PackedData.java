package cc.xfl12345.mybigdata.server.model.api.database.result;

import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;

public class PackedData <T> {
    public GlobalDataRecord globalDataRecord;
    public T content;
}
