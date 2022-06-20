package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.listener.ContextFinalizer;
import cc.xfl12345.mybigdata.server.model.database.handler.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.CoreTableCache;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.SqlErrorHandlerImpl;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.StringTypeHandlerImpl;
import cc.xfl12345.mybigdata.server.model.database.producer.GlobalDataRecordProducer;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.fasterxml.uuid.Generators;
import org.teasoft.honey.osql.core.BeeFactory;

public class TestDataHandler {
    public static void main(String[] args) throws Exception {
        DruidDataSource dataSource = TestLoadDataSource.getDataSource();
        BeeFactory beeFactory = BeeFactory.getInstance();
        beeFactory.setDataSource(dataSource);

        CoreTableCache coreTableCache = new CoreTableCache();

        GlobalDataRecordProducer globalDataRecordProducer = new GlobalDataRecordProducer();
        globalDataRecordProducer.setUuidGenerator(Generators.timeBasedGenerator());
        globalDataRecordProducer.afterPropertiesSet();

        SqlErrorHandler sqlErrorHandler = new SqlErrorHandlerImpl();

        StringTypeHandlerImpl stringTypeHandler = new StringTypeHandlerImpl();
        stringTypeHandler.setCoreTableCache(coreTableCache);
        stringTypeHandler.setGlobalDataRecordProducer(globalDataRecordProducer);
        stringTypeHandler.setSqlErrorHandler(sqlErrorHandler);
        stringTypeHandler.afterPropertiesSet();

        System.out.println(JSON.toJSONString(
            stringTypeHandler.selectStringByPrefix("t", null),
            JSONWriter.Feature.PrettyFormat
        ));

        globalDataRecordProducer.destroy();
        stringTypeHandler.destroy();

        ContextFinalizer.deregisterJdbcDriver(null);
    }
}
