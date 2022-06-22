package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.listener.ContextFinalizer;
import cc.xfl12345.mybigdata.server.model.database.handler.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.CoreTableCache;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.SqlErrorHandlerImpl;
import cc.xfl12345.mybigdata.server.model.database.handler.impl.StringTypeHandlerImpl;
import cc.xfl12345.mybigdata.server.model.database.producer.impl.GlobalDataRecordProducer;
import cc.xfl12345.mybigdata.server.model.database.result.StringTypeResult;
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

        printJSON(stringTypeHandler.selectStringByPrefix("t", null));

        StringTypeResult stringTypeResult = stringTypeHandler.selectStringByFullText("text", null);
        printJSON(stringTypeResult);
        if (stringTypeResult.getSimpleResult().equals(SimpleCoreTableCurdResult.SUCCEED)) {
            printJSON(stringTypeHandler.updateStringByGlobalId("text666", stringTypeResult.getStringContent().getGlobalId()));
            printJSON(stringTypeHandler.updateStringByFullText("text666", "text"));
        }


        globalDataRecordProducer.destroy();
        stringTypeHandler.destroy();

        ContextFinalizer.deregisterJdbcDriver(null);
    }

    public static void printJSON(Object obj) {
        System.out.println(JSON.toJSONString(obj, JSONWriter.Feature.PrettyFormat));
    }
}
