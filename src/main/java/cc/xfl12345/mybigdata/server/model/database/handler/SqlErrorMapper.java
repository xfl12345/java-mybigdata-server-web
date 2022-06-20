package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;

public interface SqlErrorMapper {
    SimpleCoreTableCurdResult getSimpleCoreTableCurdResult(String dbType, int vendorCode);
}
