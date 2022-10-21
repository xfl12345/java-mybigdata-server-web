package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.data.handler.NumberTypeHandler;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import cc.xfl12345.mybigdata.server.web.pojo.WebJsonApiResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_BASE + '/' + "content/number")
public class NumberContentController extends DataControllerBase {
    protected NumberTypeHandler numberTypeHandler;

    @Autowired
    public void setStringTypeSource(NumberTypeHandler numberTypeHandler) {
        this.numberTypeHandler = numberTypeHandler;
    }

    @GetMapping("by-id/{id:^\\w+}")
    public WebJsonApiResponseData httpGet(HttpServletResponse response, @PathVariable Object id) {
        return webApiDataErrorHandler.handle(response, id, (param) -> numberTypeHandler.selectById(param));
    }
}
