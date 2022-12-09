package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.api.AdvanceSearchMapper;
import cc.xfl12345.mybigdata.server.common.appconst.api.result.JsonApiResult;
import cc.xfl12345.mybigdata.server.common.data.condition.SingleTableCondition;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_BASE + '/' + "search")
public class SearchController {
    protected AdvanceSearchMapper advanceSearchMapper;

    @Autowired
    public void setAdvanceSearchMapper(AdvanceSearchMapper advanceSearchMapper) {
        this.advanceSearchMapper = advanceSearchMapper;
    }

    protected JsonApiResponseData searchString(String prefix) {
        JsonApiResponseData responseData = new JsonApiResponseData();
        responseData.setApiResult(JsonApiResult.SUCCEED);
        responseData.setData(advanceSearchMapper.selectStringByPrefix(prefix));
        return responseData;
    }

    protected JsonApiResponseData searchNumber(String prefix) {
        JsonApiResponseData responseData = new JsonApiResponseData();
        responseData.setApiResult(JsonApiResult.SUCCEED);
        responseData.setData(advanceSearchMapper.selectNumberByPrefix(prefix));
        return responseData;
    }

    @GetMapping("string/by-prefix/{prefix:^\\w+}")
    public JsonApiResponseData httpGetSearchString(@PathVariable String prefix) {
        return searchString(prefix);
    }

    @PostMapping("string/by-prefix/")
    public JsonApiResponseData httpPostSearchString(@RequestBody PrefixParam param) {
        return searchString(param.getPrefix());
    }

    @GetMapping("number/by-prefix/{prefix:^\\w+}")
    public JsonApiResponseData httpGetSearchNumber(@PathVariable String prefix) {
        return searchNumber(prefix);
    }

    @PostMapping("number/by-prefix/")
    public JsonApiResponseData httpPostSearchNumber(@RequestBody PrefixParam param) {
        return searchNumber(param.getPrefix());
    }

    @PostMapping("by-condition/")
    public JsonApiResponseData searchByCondition(@RequestBody SingleTableCondition condition) {
        JsonApiResponseData responseData = new JsonApiResponseData();
        responseData.setApiResult(JsonApiResult.SUCCEED);
        responseData.setData(advanceSearchMapper.selectByCondition(condition));
        return responseData;
    }

    public static class PrefixParam {
        @Getter
        @Setter
        private String prefix;
    }
}
