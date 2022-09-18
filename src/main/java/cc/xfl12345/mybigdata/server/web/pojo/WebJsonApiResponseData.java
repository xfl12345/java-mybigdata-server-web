package cc.xfl12345.mybigdata.server.web.pojo;

import cc.xfl12345.mybigdata.server.common.appconst.api.result.JsonApiResult;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;

public class WebJsonApiResponseData extends JsonApiResponseData {
    public WebJsonApiResponseData() {
        super(ApiConst.version);
    }

    public WebJsonApiResponseData(String version) {
        super(version);
    }

    public WebJsonApiResponseData(JsonApiResult apiResult) {
        super(apiResult);
    }

    public WebJsonApiResponseData(String version, JsonApiResult apiResult) {
        super(version, apiResult);
    }
}
