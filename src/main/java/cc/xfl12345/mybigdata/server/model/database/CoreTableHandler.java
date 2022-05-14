package cc.xfl12345.mybigdata.server.model.database;

import cc.xfl12345.mybigdata.server.model.database.producer.GlobalDataRecordProducer;
import cc.xfl12345.mybigdata.server.model.database.result.StringTypeResult;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.Suid;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.bee.osql.transaction.Transaction;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.SessionFactory;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class CoreTableHandler implements DisposableBean {

    @Getter
    @Setter
    protected NoArgGenerator uuidGenerator;

    protected GlobalDataRecordProducer globalDataRecordProducer;
    protected boolean keepProduce = true;
    protected Thread preProduce;
    protected Thread produceGlobalDataRecord;

    public CoreTableHandler() {
        this(Generators.timeBasedGenerator());
    }

    public CoreTableHandler(NoArgGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
        globalDataRecordProducer = new GlobalDataRecordProducer(uuidGenerator);


    }

    public UUID getNewUUID() {
        return uuidGenerator.generate();
    }





    public StringTypeResult insertString(String content) {
        StringTypeResult stringTypeResult = null;

        StringContent stringContent = new StringContent();

        Transaction transaction= SessionFactory.getTransaction();
        try {

            transaction.begin();

            SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();
//            suid.selectById()


            transaction.commit();


        } catch (BeeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            transaction.rollback();
        }

        return stringTypeResult;
    }

    @Override
    public void destroy() throws Exception {

    }
}
