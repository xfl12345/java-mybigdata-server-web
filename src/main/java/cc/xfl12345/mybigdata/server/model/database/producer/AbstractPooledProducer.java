package cc.xfl12345.mybigdata.server.model.database.producer;

import com.fasterxml.uuid.NoArgGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.LinkedBlockingDeque;

public abstract class AbstractPooledProducer <T> implements DisposableBean {
    protected LinkedBlockingDeque<T> resourcePool;

    protected volatile boolean keepProduce = true;
    protected Thread preProduceThread;
    protected Thread producingThread;

    @Getter
    @Setter
    protected volatile NoArgGenerator uuidGenerator;

    public AbstractPooledProducer() {
        resourcePool = new LinkedBlockingDeque<>(200);
    }


    @Override
    public void destroy() throws Exception {
        keepProduce = false;
        resourcePool.clear();
        producingThread.join(2000);
        resourcePool.clear();
    }
}
