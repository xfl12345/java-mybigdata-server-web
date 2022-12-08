package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.web.WebApiExecutor;
import org.springframework.beans.factory.annotation.Autowired;

public class DataControllerBase {
    protected WebApiExecutor webApiExecutor;

    @Autowired
    public void setWebApiDataErrorHandler(WebApiExecutor webApiExecutor) {
        this.webApiExecutor = webApiExecutor;
    }
}
