package cc.xfl12345.mybigdata.server.model.database.result;

import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import lombok.Getter;
import lombok.Setter;

public class ExecuteResultBase {
    @Getter
    @Setter
    protected SimpleCoreTableCurdResult simpleResult = SimpleCoreTableCurdResult.NO_EXECUTE;

    @Getter
    @Setter
    protected Exception sqlException = null;
}
