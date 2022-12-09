package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.data.source.StringTypeSource;
import cc.xfl12345.mybigdata.server.common.data.source.pojo.CommonMbdId;
import cc.xfl12345.mybigdata.server.common.pojo.IdAndValue;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
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
    public JsonApiResponseData httpGet(HttpServletResponse response, @PathVariable String id) {
        return webApiExecutor.handle(response, new CommonMbdId(id), stringTypeSource::selectById);
    }

    @PutMapping("by-id/{id:^\\w+}")
    public JsonApiResponseData httpPost(HttpServletResponse response, @PathVariable String id, @RequestBody String content) {
        IdAndValue<String> idAndValue = new IdAndValue<>();
        idAndValue.id = new CommonMbdId(id);
        idAndValue.value = content;
        return webApiExecutor.handle(response, idAndValue, (param) -> {
            stringTypeSource.updateById(param.value, param.id);
            return null;
        });
    }

    @PutMapping("")
    public JsonApiResponseData httpPost(HttpServletResponse response, @RequestBody String content) {
        return webApiExecutor.handle(response, content, stringTypeSource::insert4IdOrGetId);
    }

    @DeleteMapping("")
    public JsonApiResponseData httpDelete(HttpServletResponse response, String content) {
        return webApiExecutor.handle(response, content, stringTypeSource::delete);
    }

}
