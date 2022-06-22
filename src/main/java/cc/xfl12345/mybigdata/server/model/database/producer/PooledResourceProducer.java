package cc.xfl12345.mybigdata.server.model.database.producer;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.TimeUnit;

public interface PooledResourceProducer<T> extends DisposableBean, InitializingBean {
    T peek();

    T pop();

    T poll();

    T poll(long timeout, TimeUnit unit) throws InterruptedException;

    T take() throws InterruptedException;

    int size();
}
