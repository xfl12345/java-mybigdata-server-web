package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.appconst.api.result.JsonApiResult;
import cc.xfl12345.mybigdata.server.common.data.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.common.data.interceptor.FunctionWithException;
import cc.xfl12345.mybigdata.server.common.database.error.TableDataException;
import cc.xfl12345.mybigdata.server.common.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.web.pojo.WebJsonApiResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping("content/string")
public class ContentContoller {
    protected StringTypeHandler stringTypeHandler;

    @Autowired
    public void setStringTypeSource(StringTypeHandler stringTypeHandler) {
        this.stringTypeHandler = stringTypeHandler;
    }

    @GetMapping("by-id/{id:^\\w+}")
    public WebJsonApiResponseData httpGet(HttpServletResponse response, @PathVariable Object id) {
        return handleError(response, id, (param) -> stringTypeHandler.selectById(param));
    }

    public WebJsonApiResponseData handleError(
        HttpServletResponse httpServletResponse,
        Object param,
        FunctionWithException<Object, Object> action) {
        WebJsonApiResponseData responseData = new WebJsonApiResponseData();
        try {
            responseData.setData(action.apply(param));
            responseData.setApiResult(JsonApiResult.SUCCEED);
        } catch (TableOperationException e) {
            if (e.getAffectedRowsCount() == 0) {
                switch (e.getOperation()) {
                    case UPDATE, DELETE, RETRIEVE -> {
                        httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        responseData.setApiResult(JsonApiResult.FAILED_NOT_FOUND);
                    }
                    case CREATE -> {
                        httpServletResponse.setStatus(HttpServletResponse.SC_GONE);
                        responseData.setApiResult(JsonApiResult.FAILED);
                    }
                }
            } else {
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                responseData.setApiResult(JsonApiResult.FAILED_FORBIDDEN);
            }
        } catch (TableDataException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseData.setApiResult(JsonApiResult.FAILED_FORBIDDEN);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseData.setApiResult(JsonApiResult.OTHER_FAILED);
        }

        return responseData;
    }
}
