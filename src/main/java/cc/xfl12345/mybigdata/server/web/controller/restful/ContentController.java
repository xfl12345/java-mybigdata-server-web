package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.appconst.api.result.JsonApiResult;
import cc.xfl12345.mybigdata.server.common.data.source.DataSourceHome;
import cc.xfl12345.mybigdata.server.common.data.source.pojo.MbdId;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_BASE)
public class ContentController extends DataControllerBase {

    protected ObjectMapper jacksonObjectMapper;

    @Autowired
    public void setJacksonObjectMapper(ObjectMapper jacksonObjectMapper) {
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    protected DataSourceHome dataSourceHome;

    @Autowired
    public void setDataSourceHome(DataSourceHome dataSourceHome) {
        this.dataSourceHome = dataSourceHome;
    }

    @GetMapping("type/by-id/{id:^\\w+}")
    public JsonApiResponseData httpGetType(HttpServletRequest request, @PathVariable String id) {
        return webApiExecutor.handle(request, new MbdId(id), dataSourceHome::getDataTypeById);
    }

    // @PostMapping("content/by-id")
    // public JsonApiResponseData httpGetContent(HttpServletRequest request, @RequestBody BaseRequestObject requestObject) {
    //     switch (requestObject.operation) {
    //         case "GET" -> {
    //             GetDataByIdPayload payload = jacksonObjectMapper.convertValue(requestObject.data, GetDataByIdPayload.class);
    //             return webApiExecutor.handle(request, payload, param -> {
    //                 return dataSourceHome.getDataById(param.getId(), param.getDataRequirement());
    //             });
    //         }
    //         case "SET" -> {
    //             SetDataByIdPayload payload = jacksonObjectMapper.convertValue(requestObject.data, SetDataByIdPayload.class);
    //
    //             JsonNode jsonNode = jacksonObjectMapper.valueToTree(payload.getData());
    //             BaseMbdObject mbdObject;
    //
    //             switch (jsonNode.getNodeType()) {
    //                 case ARRAY -> {
    //                     PlainMdbGroup mdbGroup = new PlainMdbGroup()
    //                 }
    //                 case BINARY -> {
    //                 }
    //                 case BOOLEAN -> {
    //                 }
    //                 case MISSING -> {
    //                 }
    //                 case NULL -> {
    //                 }
    //                 case NUMBER -> {
    //                 }
    //                 case OBJECT -> {
    //                 }
    //                 case POJO -> {
    //                 }
    //                 case STRING -> {
    //                 }
    //             }
    //
    //
    //             return webApiExecutor.handle(request, mbdObject, param -> {
    //                 return dataSourceHome.setData(param);
    //             });
    //         }
    //
    //         default -> {
    //             return getNotSupportPayload();
    //         }
    //     }
    // }

    protected JsonApiResponseData getNotSupportPayload() {
        JsonApiResponseData responseData = webApiExecutor.getResponseDataInstanceGenerator().getNewInstance();
        responseData.setApiResult(JsonApiResult.FAILED_NOT_SUPPORT);

        return responseData;
    }

}
