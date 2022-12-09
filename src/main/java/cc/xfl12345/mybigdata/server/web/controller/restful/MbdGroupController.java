package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.data.source.GroupTypeSource;
import cc.xfl12345.mybigdata.server.common.data.source.pojo.CommonMbdId;
import cc.xfl12345.mybigdata.server.common.data.source.pojo.CommonMdbGroup;
import cc.xfl12345.mybigdata.server.common.pojo.IdAndValue;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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
    public JsonApiResponseData httpGet(HttpServletResponse response, @PathVariable String id) {
        return webApiExecutor.handle(response, new CommonMbdId(id), groupTypeSource::selectById);
    }

    @PutMapping("")
    public JsonApiResponseData httpPut(HttpServletResponse response, @RequestBody CommonMdbGroup mbdGroup) {
        if (mbdGroup.getGlobalId() != null) {
            IdAndValue<CommonMdbGroup> idAndValue = new IdAndValue<>();
            idAndValue.id = mbdGroup.getGlobalId();
            idAndValue.value = mbdGroup;
            return webApiExecutor.handle(response, idAndValue, (param) -> {
                groupTypeSource.updateById(param.value, param.id);
                return null;
            });
        } else {
            return webApiExecutor.handle(response, mbdGroup, groupTypeSource::insert4IdOrGetId);
        }
    }

    @DeleteMapping("by-id/{id:^\\w+}")
    public JsonApiResponseData httpDelete(HttpServletResponse response, @PathVariable String id) {
        return webApiExecutor.handle(response, new CommonMbdId(id), groupTypeSource::deleteById);
    }
}
