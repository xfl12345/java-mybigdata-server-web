package cc.xfl12345.mybigdata.server.model.database.error;

import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;

public interface SqlErrorMapper {
    SimpleCoreTableCurdResult getSimpleCoreTableCurdResult(String dbType, int vendorCode);
}
