package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.data.source.StringTypeSource;
import cc.xfl12345.mybigdata.server.common.pojo.IdAndValue;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_BASE + '/' + "content/string")
public class StringContentController extends DataControllerBase {
    protected StringTypeSource stringTypeSource;

    @Autowired
    public void setStringTypeSource(StringTypeSource stringTypeSource) {
        this.stringTypeSource = stringTypeSource;
    }

    @GetMapping("by-id/{id:^\\w+}")
    public JsonApiResponseData httpGet(HttpServletResponse response, @PathVariable Number id) {
        return webApiExecutor.handle(response, id, stringTypeSource::deleteById);
    }

    @PutMapping("by-id/{id:^\\w+}")
    public JsonApiResponseData httpPut(HttpServletResponse response, @PathVariable Number id, @RequestParam String content) {
        IdAndValue<String> idAndValue = new IdAndValue<>();
        idAndValue.id = id;
        idAndValue.value = content;
        return webApiExecutor.handle(response, idAndValue, (param) -> {
            stringTypeSource.updateById(param.value, param.id);
            return null;
        });
    }

    @PutMapping("")
    public JsonApiResponseData httpPut(HttpServletResponse response, @RequestParam String content) {
        return webApiExecutor.handle(response, content, stringTypeSource::insert4IdOrGetId);
    }

    @DeleteMapping("")
    public JsonApiResponseData httpDelete(HttpServletResponse response, @RequestParam String content) {
        return webApiExecutor.handle(response, content, stringTypeSource::delete);
    }

}
