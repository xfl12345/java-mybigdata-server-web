package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.web.pojo.WebApiDataErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class DataControllerBase {
    protected WebApiDataErrorHandler webApiDataErrorHandler;

    @Autowired
    public void setWebApiDataErrorHandler(WebApiDataErrorHandler webApiDataErrorHandler) {
        this.webApiDataErrorHandler = webApiDataErrorHandler;
    }
}
