package cc.xfl12345.mybigdata.server.pojo;

import java.util.concurrent.ConcurrentHashMap;

public class StringIdCache {
    protected ConcurrentHashMap<String, Long> k2v;
    protected ConcurrentHashMap<Long, String> v2k;

    public StringIdCache(int initialCapacity) {
        k2v = new ConcurrentHashMap<>(initialCapacity);
        v2k = new ConcurrentHashMap<>(initialCapacity);
    }

    public StringIdCache() {
        k2v = new ConcurrentHashMap<>();
        v2k = new ConcurrentHashMap<>();
    }

    public void put(String key, Long value) {
        if (k2v.putIfAbsent(key, value) != null) {
            throw new IllegalArgumentException("duplicate key");
        }
        if (v2k.putIfAbsent(value, key) != null) {
            throw new IllegalArgumentException("duplicate value");
        }
    }

    public Long getValue(String key) {
        return k2v.get(key);
    }

    public String getKey(long value) {
        return v2k.get(value);
    }
}
