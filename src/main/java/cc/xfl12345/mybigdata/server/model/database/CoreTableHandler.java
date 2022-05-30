package cc.xfl12345.mybigdata.server.model.database;

import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.producer.GlobalDataRecordProducer;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class CoreTableHandler implements ICoreTableHandler {
    @Getter
    @Setter
    protected volatile StringTypeHandler stringTypeHandler;

    @Getter
    @Setter
    protected volatile NoArgGenerator uuidGenerator;

    // @Getter
    // @Setter
    // protected volatile String jdbcDriverName;

    protected GlobalDataRecordProducer globalDataRecordProducer;
    protected boolean keepGoing = true;



    @Override
    public void afterPropertiesSet() throws Exception {
        if (uuidGenerator == null) {
            uuidGenerator = Generators.timeBasedGenerator();
        }

        // if (jdbcDriverName == null) {
        //     jdbcDriverName = com.mysql.cj.jdbc.Driver.class.getCanonicalName();
        // }
        globalDataRecordProducer = new GlobalDataRecordProducer(uuidGenerator);
    }

    @Override
    public void destroy() throws Exception {
        keepGoing = false;
    }

    @Override
    public UUID getNewUUID() {
        return uuidGenerator.generate();
    }


}
