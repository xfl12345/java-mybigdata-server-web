package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.data.source.NumberTypeSource;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
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
    protected NumberTypeSource numberTypeSource;

    @Autowired
    public void setStringTypeSource(NumberTypeSource numberTypeSource) {
        this.numberTypeSource = numberTypeSource;
    }

    @GetMapping("by-id/{id:^\\w+}")
    public JsonApiResponseData httpGet(HttpServletResponse response, @PathVariable Number id) {
        return webApiExecutor.handle(response, id, numberTypeSource::selectById);
    }
}
