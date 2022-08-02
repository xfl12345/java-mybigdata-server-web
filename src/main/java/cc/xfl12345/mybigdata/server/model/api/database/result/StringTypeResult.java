package cc.xfl12345.mybigdata.server.model.api.database.result;

import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import lombok.Getter;
import lombok.Setter;

public class StringTypeResult extends SingleDataResultBase {
    @Getter
    @Setter
    protected StringContent stringContent;
}
