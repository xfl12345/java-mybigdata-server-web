package cc.xfl12345.mybigdata.server.model.database.error;

import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.model.api.database.result.ExecuteResultBase;
import cc.xfl12345.mybigdata.server.model.transaction.Transaction;
import lombok.NonNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface SqlErrorAnalyst extends SqlErrorMapper {
    SimpleCoreTableCurdResult getSimpleCoreTableCurdResult(DataSource dataSource, int vendorCode) throws SQLException;

    SimpleCoreTableCurdResult getSimpleCoreTableCurdResult(Connection connection, int vendorCode) throws SQLException;

    SimpleCoreTableCurdResult getSimpleCoreTableCurdResult(@NonNull Exception exception);

    void defaultErrorHandler(@NonNull Exception e, Transaction transaction, ExecuteResultBase result);
}
