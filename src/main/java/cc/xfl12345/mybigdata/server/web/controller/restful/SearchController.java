package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.appconst.api.result.JsonApiResult;
import cc.xfl12345.mybigdata.server.common.data.condition.SingleTableCondition;
import cc.xfl12345.mybigdata.server.common.web.mapper.AdvanceSearchMapper;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import cc.xfl12345.mybigdata.server.web.pojo.WebJsonApiResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_NAME + '/' + "search")
public class SearchController {
    protected AdvanceSearchMapper advanceSearchMapper;

    @Autowired
    public void setAdvanceSearchMapper(AdvanceSearchMapper advanceSearchMapper) {
        this.advanceSearchMapper = advanceSearchMapper;
    }

    @GetMapping("string/by-prefix/{prefix:^\\w+}")
    public WebJsonApiResponseData searchString(@PathVariable String prefix) {
        WebJsonApiResponseData responseData = new WebJsonApiResponseData();
        responseData.setApiResult(JsonApiResult.SUCCEED);
        responseData.setData(advanceSearchMapper.selectStringByPrefix(prefix));
        return responseData;
    }

    @GetMapping("number/by-prefix/{prefix:^\\w+}")
    public WebJsonApiResponseData searchNumber(@PathVariable String prefix) {
        WebJsonApiResponseData responseData = new WebJsonApiResponseData();
        responseData.setApiResult(JsonApiResult.SUCCEED);
        responseData.setData(advanceSearchMapper.selectNumberByPrefix(prefix));
        return responseData;
    }

    @PostMapping("by-condition")
    public WebJsonApiResponseData searchNumber(SingleTableCondition condition) {
        WebJsonApiResponseData responseData = new WebJsonApiResponseData();
        responseData.setApiResult(JsonApiResult.SUCCEED);
        responseData.setData(advanceSearchMapper.selectByCondition(condition));
        return responseData;
    }
}
