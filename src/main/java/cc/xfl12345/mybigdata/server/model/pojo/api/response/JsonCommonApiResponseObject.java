package cc.xfl12345.mybigdata.server.model.pojo.api.response;

import cc.xfl12345.mybigdata.server.appconst.api.result.JsonApiResult;

public class JsonCommonApiResponseObject extends BaseResponseObject {
    protected int code;
    protected Object data;

    public JsonCommonApiResponseObject() {
        this.setVersion("undefined");//未定义版本号
    }

    public JsonCommonApiResponseObject(String version) {
        this.setVersion(version);
    }

    public JsonCommonApiResponseObject(JsonApiResult apiResult) {
        this();
        setApiResult(apiResult);
    }

    public JsonCommonApiResponseObject(String version, JsonApiResult apiResult) {
        this(version);
        setApiResult(apiResult);
    }

    public void setApiResult(JsonApiResult apiResult) {
        this.setSuccess(apiResult.equals(JsonApiResult.SUCCEED));
        this.setCode(apiResult.getNum());
        this.setMessage(apiResult.getName());
    }

    public void appendMessage(String msg) {
        if (getMessage() == null) {
            setMessage(msg);
        } else {
            if (getMessage().equals("")) {
                setMessage(msg);
            } else {
                setMessage(getMessage() + ";" + msg);
            }
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
