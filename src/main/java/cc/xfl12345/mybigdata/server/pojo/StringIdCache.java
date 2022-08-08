package cc.xfl12345.mybigdata.server.pojo;

import org.bson.types.ObjectId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StringIdCache extends TwoWayMap<String, ObjectId> {
    public StringIdCache(int initialCapacity) {
        super(initialCapacity);
    }

    public StringIdCache() {
    }
}
