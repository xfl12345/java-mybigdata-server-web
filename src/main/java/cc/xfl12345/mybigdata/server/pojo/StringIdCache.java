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

    public boolean remove(String key) {
        Long value = k2v.remove(key);
        return value != null && (v2k.remove(value) != null);
    }

    public boolean remove(Long value) {
        String key = v2k.remove(value);
        return key != null && (k2v.remove(key) != null);
    }

    public void clear() {
        k2v.clear();
        v2k.clear();
    }

    public void put(String key, Long value) {
        k2v.put(key, value);
        v2k.put(value, key);
    }

    // public void put(String key, Long value) {
    //     if (k2v.put(key, value) != null) {
    //         throw new IllegalArgumentException("duplicate key");
    //     }
    //     if (v2k.put(value, key) != null) {
    //         throw new IllegalArgumentException("duplicate value");
    //     }
    // }

    public Long getValue(String key) {
        return k2v.get(key);
    }

    public String getKey(Long value) {
        return v2k.get(value);
    }
}
