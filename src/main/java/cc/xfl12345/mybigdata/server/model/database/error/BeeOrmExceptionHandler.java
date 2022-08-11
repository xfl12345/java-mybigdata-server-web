package cc.xfl12345.mybigdata.server.model.database.error;

import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.model.api.database.result.ExecuteResultBase;
import lombok.NonNull;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.transaction.Transaction;

public interface BeeOrmExceptionHandler {
    SimpleCoreTableCurdResult getSimpleCoreTableCurdResult(@NonNull BeeException beeException);

    void defaultErrorHandler(@NonNull Exception e, Transaction transaction, ExecuteResultBase result);
}
