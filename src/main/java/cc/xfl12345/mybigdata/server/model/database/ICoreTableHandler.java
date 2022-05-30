package cc.xfl12345.mybigdata.server.model.database;

import cc.xfl12345.mybigdata.server.model.database.result.*;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.UUID;

public interface ICoreTableHandler extends DisposableBean, InitializingBean {
    UUID getNewUUID();


}
