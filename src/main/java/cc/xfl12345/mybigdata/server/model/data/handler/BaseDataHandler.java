package cc.xfl12345.mybigdata.server.model.data.handler;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.model.data.interceptor.DataHandlerInterceptor;
import cc.xfl12345.mybigdata.server.model.data.interceptor.DataHandlerInterceptorManager;
import org.springframework.beans.factory.InitializingBean;

public class BaseDataHandler implements InitializingBean {
    protected String fieldCanNotBeNullMessageTemplate = "Property [%s] can not be null!";

    @Override
    public void afterPropertiesSet() throws Exception {
    }



    public BaseDataHandler() {

    }

    /**
     * 管理拦截器
     * @param op2Manager 增删查改拦截器
     * @param indexOfInterceptor 拦截器下标
     * @param interceptorType 应用拦截器到哪个操作类型
     * @param interceptor 拦截器
     */
    public void interceptorCURD(
        CURD op2Manager,
        int indexOfInterceptor,
        CURD interceptorType,
        DataHandlerInterceptor interceptor) throws Exception {
        switch (op2Manager) {
            case CREATE -> {
                switch (interceptorType) {
                    case CREATE -> {

                    }
                    case UPDATE -> {

                    }
                }
            }
        }

    }


    public class IdAndValue<ValueType> {
        public Object id;
        public ValueType value;
    }

    public final DataHandlerInterceptorManager insertAndReturnId =
        new DataHandlerInterceptorManager();

    public final DataHandlerInterceptorManager insert =
        new DataHandlerInterceptorManager();

    public final DataHandlerInterceptorManager insertBatch =
        new DataHandlerInterceptorManager();

    public final DataHandlerInterceptorManager selectId =
        new DataHandlerInterceptorManager();

    public final DataHandlerInterceptorManager selectById =
        new DataHandlerInterceptorManager();

    public final DataHandlerInterceptorManager updateById =
        new DataHandlerInterceptorManager();

    public final DataHandlerInterceptorManager deleteById =
        new DataHandlerInterceptorManager();
}
