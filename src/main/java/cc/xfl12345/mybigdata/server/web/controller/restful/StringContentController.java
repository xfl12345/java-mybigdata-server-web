package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.data.source.StringTypeSource;
import cc.xfl12345.mybigdata.server.common.data.source.pojo.MbdId;
import cc.xfl12345.mybigdata.server.common.pojo.IdAndValue;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

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
    public JsonApiResponseData httpGet(HttpServletRequest request, @PathVariable String id) {
        return webApiExecutor.handle(request, new MbdId(id), stringTypeSource::selectById);
    }

    @PutMapping("by-id/{id:^\\w+}")
    public JsonApiResponseData httpPost(HttpServletRequest request, @PathVariable String id, @RequestBody String content) {
        IdAndValue<String> idAndValue = new IdAndValue<>();
        idAndValue.id = new MbdId(id);
        idAndValue.value = content;
        return webApiExecutor.handle(request, idAndValue, (param) -> {
            stringTypeSource.updateById(param.value, param.id);
            return null;
        });
    }

    @PutMapping("")
    public JsonApiResponseData httpPost(HttpServletRequest request, @RequestBody String content) {
        return webApiExecutor.handle(request, content, stringTypeSource::selectIdOrInsert4Id);
    }

    @DeleteMapping("")
    public JsonApiResponseData httpDelete(HttpServletRequest request, String content) {
        return webApiExecutor.handle(request, content, stringTypeSource::delete);
    }

}
