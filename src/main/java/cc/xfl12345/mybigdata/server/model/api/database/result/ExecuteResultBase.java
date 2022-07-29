package cc.xfl12345.mybigdata.server.model.api.database.result;

import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class ExecuteResultBase {
    @Getter
    @Setter
    protected SimpleCoreTableCurdResult simpleResult = SimpleCoreTableCurdResult.NO_EXECUTE;

    @Getter
    @Setter
    protected Exception sqlException = null;

    @Getter
    @Setter
    protected String message = null;

    public void setUnknowResultWithException(@NonNull Exception e) {
        sqlException = e;
        setSimpleResult(SimpleCoreTableCurdResult.UNKNOWN_FAILED);
    }
}
