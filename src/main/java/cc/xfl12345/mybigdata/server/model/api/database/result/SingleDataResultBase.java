package cc.xfl12345.mybigdata.server.model.api.database.result;

import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import lombok.Getter;
import lombok.Setter;

public class SingleDataResultBase extends ExecuteResultBase {
    @Getter
    @Setter
    protected GlobalDataRecord globalDataRecord = null;
}
