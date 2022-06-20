package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.model.database.result.ExecuteResultBase;
import lombok.NonNull;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public interface SqlErrorHandler extends SqlErrorMapper {
    SimpleCoreTableCurdResult getSimpleCoreTableCurdResult(DataSource dataSource, int vendorCode) throws SQLException;

    SimpleCoreTableCurdResult getSimpleCoreTableCurdResult(Connection connection, int vendorCode) throws SQLException;

    SimpleCoreTableCurdResult getSimpleCoreTableCurdResult(@NonNull BeeException beeException);

    void defaultErrorHandler(@NonNull Exception e, Transaction transaction, ExecuteResultBase result);
}
