package cc.xfl12345.mybigdata.server.model.database.producer;

import com.fasterxml.uuid.NoArgGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractPooledProducer <T> implements DisposableBean {
    protected LinkedBlockingDeque<T> resourcePool;

    protected volatile boolean keepProduce = true;
    protected Thread preProduceThread;
    protected Thread producingThread;

    public AbstractPooledProducer() {
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


    public T peek() {
        return resourcePool.peek();
    }

    public T pop() {
        return resourcePool.pop();
    }

    public T poll() {
        return resourcePool.poll();
    }

    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        return resourcePool.poll(timeout, unit);
    }

    public T take() throws InterruptedException {
        return resourcePool.take();
    }

    public int size() {
        return resourcePool.size();
    }
}
