package cc.xfl12345.mybigdata.server.model.data.handler;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.model.data.interceptor.DataHandlerInterceptor;
import cc.xfl12345.mybigdata.server.model.data.interceptor.DataHandlerInterceptorManager;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.CopyOnWriteArrayList;

public class BaseDataHandler implements InitializingBean {
    protected String fieldCanNotBeNullMessageTemplate = "Property [%s] can not be null!";

    @Override
    public void afterPropertiesSet() throws Exception {
    }


    public BaseDataHandler() {
        inserts = new CopyOnWriteArrayList<>(new DataHandlerInterceptorManager[] {
            insert, insertAndReturnId, insertBatch
        });
        selects = new CopyOnWriteArrayList<>(new DataHandlerInterceptorManager[] {
            selectId, selectById
        });
        updates = new CopyOnWriteArrayList<>(new DataHandlerInterceptorManager[] {
            updateById
        });
        deletes = new CopyOnWriteArrayList<>(new DataHandlerInterceptorManager[] {
            deleteById
        });
    }

    public CopyOnWriteArrayList<DataHandlerInterceptorManager> getManagers(CURD interceptorType) {
        CopyOnWriteArrayList<DataHandlerInterceptorManager> managers = null;
        switch (interceptorType) {
            case CREATE -> managers = inserts;
            case UPDATE -> managers = updates;
            case RETRIEVE -> managers = selects;
            case DELETE -> managers = deletes;
        }

        return managers;
    }

    /**
     * 统一添加拦截器
     * @param indexOfInterceptor 拦截器下标
     * @param interceptorType 应用拦截器到哪个操作类型
     * @param interceptor 拦截器
     */
    public void addInterceptor(int indexOfInterceptor, CURD interceptorType, DataHandlerInterceptor interceptor)
        throws Exception {
        getManagers(interceptorType).forEach(item -> item.add(indexOfInterceptor, interceptor));
    }

    /**
     * 统一更新拦截器
     * @param indexOfInterceptor 拦截器下标
     * @param interceptorType 应用拦截器到哪个操作类型
     * @param interceptor 拦截器
     */
    public void updateInterceptor(int indexOfInterceptor, CURD interceptorType, DataHandlerInterceptor interceptor)
        throws Exception {
        getManagers(interceptorType).forEach(item -> item.set(indexOfInterceptor, interceptor));
    }

    /**
     * 统一删除拦截器
     * @param indexOfInterceptor 拦截器下标
     * @param interceptorType 应用拦截器到哪个操作类型
     */
    public void removeInterceptor(int indexOfInterceptor, CURD interceptorType)
        throws Exception {
        getManagers(interceptorType).forEach(item -> item.remove(indexOfInterceptor));
    }

    public final CopyOnWriteArrayList<DataHandlerInterceptorManager> inserts;
    public final CopyOnWriteArrayList<DataHandlerInterceptorManager> selects;
    public final CopyOnWriteArrayList<DataHandlerInterceptorManager> updates;
    public final CopyOnWriteArrayList<DataHandlerInterceptorManager> deletes;


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
