package cc.xfl12345.mybigdata.server.model.api.database.result;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.Setter;

public class JsonTypeResult extends SingleDataResultBase {
    @Getter
    @Setter
    protected JSONObject jsonObject = null;
}
