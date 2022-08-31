package cc.xfl12345.mybigdata.server.model.transaction;

import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;

public interface Transaction {
    void begin() throws Exception;

    GlobalDataRecord lockGlobalDataRecord(GlobalDataRecord globalDataRecord) throws Exception;

    void commit();
    void rollback();

    int getTransactionIsolationLevel();
    boolean isReadOnly();
}
