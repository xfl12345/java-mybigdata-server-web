package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.api.IdViewer;
import cc.xfl12345.mybigdata.server.common.data.source.GlobalDataRecordDataSource;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_BASE + '/' + "content")
public class ContentController extends DataControllerBase {
    protected GlobalDataRecordDataSource globalDataRecordDataSource;

    @Autowired
    public void setGlobalDataRecordDataSource(GlobalDataRecordDataSource globalDataRecordDataSource) {
        this.globalDataRecordDataSource = globalDataRecordDataSource;
    }

    protected IdViewer idViewer;

    @Autowired
    public void setIdViewer(IdViewer idViewer) {
        this.idViewer = idViewer;
    }

    @GetMapping("type/by-id/{id:^\\w+}")
    public JsonApiResponseData httpGet(HttpServletResponse response, @PathVariable Object id) {
        return webApiExecutor.handle(response, id, idViewer::getDataTypeById);
    }

    // @PostMapping
    // public JsonApiResponseData httpPost(HttpServletResponse response) {
    //
    //     return webApiExecutor.handle(response, )
    // }

}
