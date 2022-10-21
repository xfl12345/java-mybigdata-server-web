package cc.xfl12345.mybigdata.server.web.pojo;

import cc.xfl12345.mybigdata.server.common.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.common.appconst.api.result.JsonApiResult;
import cc.xfl12345.mybigdata.server.common.data.interceptor.FunctionWithException;
import cc.xfl12345.mybigdata.server.common.database.error.SqlErrorAnalyst;
import cc.xfl12345.mybigdata.server.common.database.error.TableDataException;
import cc.xfl12345.mybigdata.server.common.database.error.TableOperationException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

@Slf4j
public class WebApiDataErrorHandler {
    protected SqlErrorAnalyst sqlErrorAnalyst;

    public SqlErrorAnalyst getSqlErrorAnalyst() {
        return sqlErrorAnalyst;
    }

    public void setSqlErrorAnalyst(SqlErrorAnalyst sqlErrorAnalyst) {
        this.sqlErrorAnalyst = sqlErrorAnalyst;
    }

    public WebJsonApiResponseData handle(
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
                responseData.setMessage(e.getMessage());
            }
        } catch (TableDataException e) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            responseData.setApiResult(JsonApiResult.FAILED_FORBIDDEN);
            responseData.setMessage(e.getMessage());
        } catch (Exception e) {
            if (sqlErrorAnalyst == null) {
                log.warn(e.getMessage(), e);
                httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                responseData.setApiResult(JsonApiResult.OTHER_FAILED);
            } else {
                SimpleCoreTableCurdResult curdResult = sqlErrorAnalyst.getSimpleCoreTableCurdResult(e);
                responseData.setApiResult(JsonApiResult.FAILED);
                responseData.setMessage(curdResult.name());
            }
        }

        return responseData;
    }


}
