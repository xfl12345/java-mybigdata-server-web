package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.appconst.api.result.JsonApiResult;
import cc.xfl12345.mybigdata.server.common.data.condition.SingleTableCondition;
import cc.xfl12345.mybigdata.server.common.web.mapper.AdvanceSearchMapper;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import cc.xfl12345.mybigdata.server.web.pojo.WebJsonApiResponseData;
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

    protected WebJsonApiResponseData searchString(String prefix) {
        WebJsonApiResponseData responseData = new WebJsonApiResponseData();
        responseData.setApiResult(JsonApiResult.SUCCEED);
        responseData.setData(advanceSearchMapper.selectStringByPrefix(prefix));
        return responseData;
    }

    protected WebJsonApiResponseData searchNumber(String prefix) {
        WebJsonApiResponseData responseData = new WebJsonApiResponseData();
        responseData.setApiResult(JsonApiResult.SUCCEED);
        responseData.setData(advanceSearchMapper.selectNumberByPrefix(prefix));
        return responseData;
    }

    @GetMapping("string/by-prefix/{prefix:^\\w+}")
    public WebJsonApiResponseData httpGetSearchString(@PathVariable String prefix) {
        return searchString(prefix);
    }

    @PostMapping("string/by-prefix/")
    public WebJsonApiResponseData httpPostSearchString(@RequestBody PrefixParam param) {
        return searchString(param.getPrefix());
    }

    @GetMapping("number/by-prefix/{prefix:^\\w+}")
    public WebJsonApiResponseData httpGetSearchNumber(@PathVariable String prefix) {
        return searchNumber(prefix);
    }

    @PostMapping("number/by-prefix/")
    public WebJsonApiResponseData httpPostSearchNumber(@RequestBody PrefixParam param) {
        return searchNumber(param.getPrefix());
    }

    @PostMapping("by-condition")
    public WebJsonApiResponseData searchByCondition(@RequestBody SingleTableCondition condition) {
        WebJsonApiResponseData responseData = new WebJsonApiResponseData();
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
