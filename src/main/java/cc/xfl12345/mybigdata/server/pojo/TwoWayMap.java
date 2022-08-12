package cc.xfl12345.mybigdata.server.pojo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TwoWayMap <K,V> {
    protected ConcurrentHashMap<K, V> k2v;
    protected ConcurrentHashMap<V, K> v2k;

    public TwoWayMap(int initialCapacity) {
        k2v = new ConcurrentHashMap<>(initialCapacity);
        v2k = new ConcurrentHashMap<>(initialCapacity);
    }

    public TwoWayMap() {
        k2v = new ConcurrentHashMap<>();
        v2k = new ConcurrentHashMap<>();
    }

    public boolean removeKey(K key) {
        V value = k2v.remove(key);
        return value != null && (v2k.remove(value) != null);
    }

    public boolean removeValue(V value) {
        K key = v2k.remove(value);
        return key != null && (k2v.remove(key) != null);
    }

    public void clear() {
        k2v.clear();
        v2k.clear();
    }

    public void put(K key, V value) {
        k2v.put(key, value);
        v2k.put(value, key);
    }

    // public void put(K key, V value) {
    //     if (k2v.put(key, value) != null) {
    //         throw new IllegalArgumentException("duplicate key");
    //     }
    //     if (v2k.put(value, key) != null) {
    //         throw new IllegalArgumentException("duplicate value");
    //     }
    // }

    public V getValue(K key) {
        return k2v.get(key);
    }

    public K getKey(V value) {
        return v2k.get(value);
    }

    public Map<K, V> getKey2ValueMap() {
        return k2v;
    }

    public Map<V, K> getValue2KeyMap() {
        return v2k;
    }
}
