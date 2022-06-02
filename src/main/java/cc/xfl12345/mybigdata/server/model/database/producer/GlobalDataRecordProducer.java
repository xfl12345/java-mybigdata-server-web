package cc.xfl12345.mybigdata.server.model.database.producer;

import cc.xfl12345.mybigdata.server.model.database.constant.GlobalDataRecordConstant;
import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.bee.osql.transaction.Transaction;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;
import org.teasoft.honey.osql.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GlobalDataRecordProducer extends AbstractPooledProducer<GlobalDataRecord> {
    @Getter
    @Setter
    protected volatile NoArgGenerator uuidGenerator;

    public UUID getNewUUID() {
        return uuidGenerator.generate();
    }

    public GlobalDataRecordProducer(NoArgGenerator uuidGenerator) {
        super();
        this.uuidGenerator = uuidGenerator;
        GlobalDataRecordProducer myself = this;
        producingThread = new Thread() {
            @Override
            public void run() {
                // 先缓存 SQL条件判断组件
                Condition condition = new ConditionImpl();
                // 如果 "table_name" 字段是空的，那么意味着它将被回收用作它途。
                condition.op(GlobalDataRecordConstant.DB_TABLE_NAME, Op.eq, null);

                while (keepProduce) {
                    // 开启事务，防止 global_id 冲突
                    Transaction transaction = SessionFactory.getTransaction();
                    try {
                        transaction.begin();
                        transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
                        HoneyFactory honeyFactory = new HoneyFactory();
                        // 禁用缓存
                        honeyFactory.setCache(new NoCache());
                        SuidRich suid = honeyFactory.getSuidRich();

                        // 先取出，投入到资源池里
                        List<GlobalDataRecord> items = suid.select(new GlobalDataRecord(), condition);
                        int itemsCount = items.size();
                        for (int i = 0; keepProduce && i < itemsCount; i++) {
                            GlobalDataRecord item = items.get(i);
                            try {
                                boolean isPut = false;
                                while (keepProduce && !isPut) {
                                    isPut = resourcePool.offerLast(item, 3, TimeUnit.SECONDS);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                log.error(e.getMessage());
                            }
                        }

                        // 不够再凑数
                        if (keepProduce) {
                            int remainingCapacity = resourcePool.remainingCapacity();
                            // 预分配至少 2倍 空间，防止触发扩容
                            ArrayList<GlobalDataRecord> records = new ArrayList<>((remainingCapacity << 1));
                            for (int i = 0; keepProduce && i < remainingCapacity; i++) {
                                GlobalDataRecord globalDataRecord = new GlobalDataRecord();
                                globalDataRecord.setUuid(myself.uuidGenerator.generate().toString());
                                records.add(globalDataRecord);
                            }
                            if (keepProduce && records.size() > 0) {
                                suid.insert(records);
                            }
                        }
                        transaction.commit();
                    } catch (BeeException e) {
                        log.error(e.getMessage());
                        transaction.rollback();
                    }

                    // try {
                    //     transaction.begin();
                    //     transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
                    //     HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
                    //     SuidRich suid = honeyFactory.getSuidRich();
                    //
                    //     transaction.commit();
                    // } catch (BeeException e) {
                    //     log.error(e.getMessage());
                    //     transaction.rollback();
                    // }
                }
            }
        };
        preProduceThread = new Thread() {
            @Override
            public void run() {
                if (keepProduce) {
                    producingThread.start();
                }
            }
        };
        preProduceThread.start();
    }
}
