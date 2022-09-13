package cc.xfl12345.mybigdata.server.web.model.checker;

import java.util.Map;
import java.util.TreeMap;

public class RowBoundsHelper {
    protected Integer offset = null;
    protected Integer limit = null;

    protected RowBoundsHelper(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public RowBoundsHelper(Object offset, Object limit) {
        if (offset instanceof String) {
            this.offset = Integer.valueOf((String) offset);
        } else if (offset instanceof Integer) {
            this.offset = (Integer) offset;
        }
        if (limit instanceof String) {
            this.limit = Integer.valueOf((String) limit);
        } else if (limit instanceof Integer) {
            this.limit = (Integer) limit;
        }
    }

    public RowBoundsHelper(Map<String, Object> map) {
        this(map.get("offset"), map.get("limit"));
    }

    public boolean IsAllSet() {
        return offset != null && limit != null;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new TreeMap<>();
        map.put("offset", offset);
        map.put("limit", limit);
        return map;
    }
}
