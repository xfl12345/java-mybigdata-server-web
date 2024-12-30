package cc.xfl12345.mybigdata.server.web.model.api;

import cc.xfl12345.mybigdata.server.common.data.source.pojo.MbdId;
import lombok.Getter;
import lombok.Setter;

public class SetDataByIdPayload {
    @Getter
    @Setter
    private MbdId id;

    @Getter
    @Setter
    private Object data;
}
