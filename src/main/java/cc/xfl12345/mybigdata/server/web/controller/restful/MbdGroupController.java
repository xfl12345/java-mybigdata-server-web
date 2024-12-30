package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.data.source.GroupTypeSource;
import cc.xfl12345.mybigdata.server.common.data.source.pojo.MbdGroup;
import cc.xfl12345.mybigdata.server.common.data.source.pojo.MbdId;
import cc.xfl12345.mybigdata.server.common.data.source.pojo.PlainMdbGroup;
import cc.xfl12345.mybigdata.server.common.pojo.IdAndValue;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_BASE + '/' + "content/group")
public class MbdGroupController extends DataControllerBase {
    protected GroupTypeSource groupTypeSource;

    @Autowired
    public void setGroupTypeSource(GroupTypeSource groupTypeSource) {
        this.groupTypeSource = groupTypeSource;
    }

    @GetMapping("by-id/{id:^\\w+}")
    public JsonApiResponseData httpGet(HttpServletRequest request, @PathVariable String id) {
        return webApiExecutor.handle(request, new MbdId(id), groupTypeSource::selectById);
    }

    @PutMapping("")
    public JsonApiResponseData httpPut(HttpServletRequest request, @RequestBody PlainMdbGroup mbdGroup) {
        if (mbdGroup.getGlobalId() != null) {
            IdAndValue<MbdGroup> idAndValue = new IdAndValue<>();
            idAndValue.id = mbdGroup.getGlobalId();
            idAndValue.value = mbdGroup;
            return webApiExecutor.handle(request, idAndValue, (param) -> {
                groupTypeSource.updateById(param.value, param.id);
                return null;
            });
        } else {
            return webApiExecutor.handle(request, mbdGroup, groupTypeSource::insertAndReturnId);
        }
    }

    @DeleteMapping("by-id/{id:^\\w+}")
    public JsonApiResponseData httpDelete(HttpServletRequest request, @PathVariable String id) {
        return webApiExecutor.handle(request, new MbdId(id), groupTypeSource::deleteById);
    }
}
