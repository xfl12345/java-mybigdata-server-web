package cc.xfl12345.mybigdata.server.utility;

import cc.xfl12345.mybigdata.server.appconst.api.result.LoginApiResult;
import cc.xfl12345.mybigdata.server.model.api.response.JsonCommonApiResponseObject;

public class ApiResultUtil {
    public static void convert(LoginApiResult loginApiResult, JsonCommonApiResponseObject responseObject) {
        responseObject.setCode(loginApiResult.getNum());
        responseObject.appendMessage(loginApiResult.getName());
    }
}
