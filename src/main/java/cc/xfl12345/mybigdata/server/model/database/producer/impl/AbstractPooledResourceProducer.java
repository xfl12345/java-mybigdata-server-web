package cc.xfl12345.mybigdata.server.model.database.producer.impl;

import cc.xfl12345.mybigdata.server.model.database.producer.PooledResourceProducer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractPooledResourceProducer<T> implements PooledResourceProducer<T> {
    protected LinkedBlockingDeque<T> resourcePool;

    protected volatile boolean keepProduce = true;
    protected Thread preProduceThread;
    protected Thread producingThread;

    public AbstractPooledResourceProducer() {
        resourcePool = new LinkedBlockingDeque<>(200);
    }


    @Override
    public void destroy() throws Exception {
        keepProduce = false;
        resourcePool.clear();
        try {
            if (producingThread.isAlive()) {
                producingThread.join(2000);
            }
        } catch (Exception exceptione) {
            log.error(exceptione.getMessage());
        }
        resourcePool.clear();
    }


    @Override
    public T peek() {
        return resourcePool.peek();
    }

    @Override
    public T pop() {
        return resourcePool.pop();
    }

    @Override
    public T poll() {
        return resourcePool.poll();
    }

    @Override
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        return resourcePool.poll(timeout, unit);
    }

    @Override
    public T take() throws InterruptedException {
        return resourcePool.take();
    }

    @Override
    public int size() {
        return resourcePool.size();
    }
}
