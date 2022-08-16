package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.model.database.handler.DataHandlerInterceptor;
import cc.xfl12345.mybigdata.server.model.database.handler.DataHandlerInterceptorManager;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public class BaseDataHandler<IdType, ValueType> implements InitializingBean {
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
        DataHandlerInterceptor<JSONObject, Object> interceptor) throws Exception {
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


    public class IdAndValue {
        public IdType id;
        public ValueType value;
    }

    public final DataHandlerInterceptorManager<ValueType, IdType> insertAndReturnId =
        new DataHandlerInterceptorManager<>();

    public final DataHandlerInterceptorManager<ValueType, IdType> insertOrSelect4Id =
        new DataHandlerInterceptorManager<>();

    public final DataHandlerInterceptorManager<ValueType, Long> insert =
        new DataHandlerInterceptorManager<>();

    public final DataHandlerInterceptorManager<List<ValueType>, Long> insertBatch =
        new DataHandlerInterceptorManager<>();

    public final DataHandlerInterceptorManager<ValueType, IdType> selectId =
        new DataHandlerInterceptorManager<>();

    public final DataHandlerInterceptorManager<IdType, ValueType> selectById =
        new DataHandlerInterceptorManager<>();

    public final DataHandlerInterceptorManager<IdAndValue, Void> updateById =
        new DataHandlerInterceptorManager<>();

    public final DataHandlerInterceptorManager<IdType, Void> deleteById =
        new DataHandlerInterceptorManager<>();
}
