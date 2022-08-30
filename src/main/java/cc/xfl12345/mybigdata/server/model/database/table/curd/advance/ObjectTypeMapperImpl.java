package cc.xfl12345.mybigdata.server.model.database.table.curd.advance;

import cc.xfl12345.mybigdata.server.model.database.table.curd.ObjectContentMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.ObjectRecordMapper;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.ObjectTypeMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ObjectTypeMapperImpl extends ObjectTypeMapper {
    @Getter
    @Setter
    protected ObjectContentMapper objectContentMapper;

    @Getter
    @Setter
    protected ObjectRecordMapper objectRecordMapper;


    /**
     * @return
     */
    @Override
    public int size() {
        // return objectRecordMapper.se;
        return 0;
    }

    /**
     * @return
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * @param key key whose presence in this map is to be tested
     * @return
     */
    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    /**
     * @param value value whose presence in this map is to be tested
     * @return
     */
    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    /**
     * @param key the key whose associated value is to be returned
     * @return
     */
    @Override
    public Object get(Object key) {
        return null;
    }

    /**
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return
     */
    @Override
    public Object put(String key, Object value) {
        return null;
    }

    /**
     * @param key key whose mapping is to be removed from the map
     * @return
     */
    @Override
    public Object remove(Object key) {
        return null;
    }

    /**
     * @param m mappings to be stored in this map
     */
    @Override
    public void putAll(Map<? extends String, ?> m) {

    }

    /**
     *
     */
    @Override
    public void clear() {

    }

    /**
     * @return
     */
    @Override
    public Set<String> keySet() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Collection<Object> values() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }
}
