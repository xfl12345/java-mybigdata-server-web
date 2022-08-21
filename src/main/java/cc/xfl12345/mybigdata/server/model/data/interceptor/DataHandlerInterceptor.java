package cc.xfl12345.mybigdata.server.model.data.interceptor;

import lombok.Getter;
import lombok.Setter;

public class DataHandlerInterceptor {
    @Getter
    @Setter
    protected boolean executeBranch = false;

    /**
     * 在执行操作之前，先处理一些事情。如果返回 false。则不继续冒泡，并判断是否执行分支操作。
     */
    public boolean beforeAction(Object param) throws Exception {
        return true;
    }

    /**
     * 分支操作。
     */
    public Object branchAction(Object param) throws Exception {
        return null;
    }

    /**
     * 在操作完成之后再做点事情。
     *
     * @param actionInputData  传入参数
     * @param actionOutputData 返回值
     */
    public void afterAction(Object actionInputData, Object actionOutputData) throws Exception {
    }
}
