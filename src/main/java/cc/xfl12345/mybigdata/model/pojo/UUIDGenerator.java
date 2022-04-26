package cc.xfl12345.mybigdata.model.pojo;

import com.fasterxml.uuid.Generators;

public class UUIDGenerator extends Generators {
    public UUIDGenerator() {
        super();
        Generators.timeBasedGenerator();
    }
}
