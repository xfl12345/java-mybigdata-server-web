package cc.xfl12345.mybigdata.server.model.database.result;

import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import lombok.Getter;
import lombok.Setter;

public class ExecuteResultBase {
    @Getter
    @Setter
    protected GlobalDataRecord globalDataRecord = null;

    @Getter
    @Setter
    protected Exception sqlException = null;
}
