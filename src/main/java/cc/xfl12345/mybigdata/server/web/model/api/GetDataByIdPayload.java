package cc.xfl12345.mybigdata.server.web.model.api;

import cc.xfl12345.mybigdata.server.common.data.requirement.DataRequirementPack;
import cc.xfl12345.mybigdata.server.common.data.source.pojo.MbdId;
import lombok.Getter;
import lombok.Setter;

public class GetDataByIdPayload {
    @Getter
    @Setter
    private MbdId id;

    @Getter
    @Setter
    private DataRequirementPack dataRequirement;
}
