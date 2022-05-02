package cc.xfl12345.mybigdata.server.pojo;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 专门用于存储偏向常量、字段类型独立不重复的数据，提供高效查询。
 * 偏向只增不减。（非线程安全）
 */
public class CrazyAddOnlyDatabase<T> {
    /**
     * 记录每一列的类型（有序）
     */
    protected List<Type> myColumnTypeList = new ArrayList<Type>();

    /**
     * 记录每一列的反射类型（有序）
     */
    protected List<Field> fieldList = new ArrayList<>();

    /**
     * 记录每一列的类型 的 字段名称（有序）
     */
    protected List<String> myColumnNameList = new ArrayList<>();

    /**
     * 每一列的引索。以对象里的每一个字段的值作为 键，值 是 行下标。
     */
    protected List<ConcurrentHashMap<Object, List<Integer>>> rowIndexDatabase =
        new ArrayList<>();

    /**
     * 记录每一行的对象
     */
    protected CopyOnWriteArrayList<T> rowList = new CopyOnWriteArrayList<T>();

    /**
     * 字段名称做主键，加速引索查询
     */
    protected Map<String, Integer> columnNameListCache = new HashMap<String, Integer>();

    /**
     * Type 字段做主键，加速引索查询。
     * （注意！这只会引索到同类型的其中一个！
     * 最好是在类型不重复的情况下使用）
     */
    protected Map<Type, Integer> columnTypeListCache = new HashMap<Type, Integer>();

    /**
     * 是否为字段值为 null 的 字段 添加引索
     */
    @Getter
    @Setter
    public boolean noIndex4Null;

    @SuppressWarnings("unchecked")
    public Class<T> getTempalteType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public CrazyAddOnlyDatabase() {
        this(false);
    }

    public CrazyAddOnlyDatabase(boolean noIndex4Null) {
        Class<T> tempalteClass = getTempalteType();
        Field[] fields = tempalteClass.getFields();
        this.noIndex4Null = noIndex4Null;

        int column = 0;

        for (int j = 0; j < fields.length; j++, column++) {
            Field field = fields[j];
            fieldList.add(field);
            myColumnTypeList.add(field.getType());
            myColumnNameList.add(field.getName());
            columnNameListCache.put(field.getName(), column);
            columnTypeListCache.put(field.getType(), column);
            rowIndexDatabase.add(new ConcurrentHashMap<>());
        }
    }

    public int GetColumnIndexByFieldName(String fieldName) {
        return columnNameListCache.get(fieldName);
    }

    public int GetColumnIndexByFieldType(Type type) {
        return columnTypeListCache.get(type);
    }

    public ArrayList<?> GetListByColumnIndex(int column) {
        Field field = fieldList.get(column);
        ArrayList<? super Object> result = new ArrayList<>();
        for (T item : rowList) {
            try {
                result.add(field.get(item));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public <T2> ArrayList<T2> GetListByColumnIndex(int column, Class<T2> cls) {
        Field field = fieldList.get(column);
        ArrayList<T2> result = new ArrayList<>();
        for (T item : rowList) {
            try {
                result.add((T2) field.get(item));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 按 字段类型 查询列记录。
     * （注意！这只会引索到同类型的其中一个！
     * 最好是在类型不重复的情况下使用）
     */
    public <T2> ArrayList<T2> GetListByFieldType(Class<T2> cls) {
        return GetListByColumnIndex(GetColumnIndexByFieldType(cls), cls);
    }

    protected List<T> queryRowByIndex(Object value, int column) {
        List<T> result = new ArrayList<>();
        ConcurrentHashMap<Object, List<Integer>> db = rowIndexDatabase.get(column);
        List<Integer> rows = db.get(value);
        if (rows != null) {
            for (Integer row : rows) {
                result.add(rowList.get(row));
            }
        }

        return result;
    }

    /**
     * 按 字段类型 和 确切值 查询行记录。
     * （注意！这只会引索到同类型的其中一个！
     * 最好是在类型不重复的情况下使用）
     */
    public List<T> QueryRow(Type fieldType, Object value) {
        if (!columnTypeListCache.containsKey(fieldType))
            return new ArrayList<T>();

        int column = columnTypeListCache.get(fieldType);
        return queryRowByIndex(value, column);
    }

    /**
     * 按 字段名称 和 确切值 查询行记录。
     */
    public List<T> QueryRow(String fieldName, Object value) {
        if (!columnNameListCache.containsKey(fieldName))
            return new ArrayList<T>();

        int column = columnNameListCache.get(fieldName);
        return queryRowByIndex(value, column);
    }

    /**
     * 直接按 确切值（及其类型） 查询行记录。
     * （注意！这只会引索到同类型的其中一个！
     * 最好是在类型不重复的情况下使用。
     * 自动判断值的类型，其中不包括空值，如遇空值，将会直接返回 null）
     */
    public List<T> QueryRow(Object value) {
        return value == null ? null : QueryRow(value.getClass(), value);
    }

    /**
     * 直接按 确切值（及其类型） 查询第一个匹配的行记录。
     * （注意！这只会引索到同类型的其中一个！
     * 最好是在类型不重复的情况下使用。
     * 自动判断值的类型，其中不包括空值，如遇空值，将会直接返回 null）
     */
    public T QueryFirstRow(Object value) {
        if (value == null) {
            return null;
        }

        T result = null;
        Type fieldType = value.getClass();
        if (columnTypeListCache.containsKey(fieldType)) {
            // 定位 到 列
            int column = columnTypeListCache.get(fieldType);
            // 检索同值的 行 的 下标
            ConcurrentHashMap<Object, List<Integer>> db = rowIndexDatabase.get(column);
            List<Integer> rows = db.get(value);
            if (rows != null) {
                //添加检索到的行
                result = rowList.get(rows.get(0));
            }
        }

        return result;
    }

    /**
     * 直接按 确切值 和 确切列 查询第一个匹配的行记录。
     * （注意！这只会引索到同列同值中一个！
     * 最好是在 该列的值 不重复的情况下使用。）
     */
    public T QueryFirstRow(Object value, int column) {
        if (value == null) {
            return null;
        }

        T result = null;
        // 检索同值的 行 的 下标
        ConcurrentHashMap<Object, List<Integer>> db = rowIndexDatabase.get(column);
        List<Integer> rows = db.get(value);
        if (rows != null) {
            //添加检索到的行
            result = rowList.get(rows.get(0));
        }

        return result;
    }


    public void add(T item) throws IllegalAccessException {
        boolean isFound = false;
        int row = rowList.size();

        rowList.add(item);
        for (; row < rowList.size(); row++) {
            if (rowList.get(row).equals(item)) {
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            throw new IllegalAccessException("List member changed while adding item is not complete.");
        }

        //遍历每一列
        for (int column = 0; column < fieldList.size(); column++) {
            Field field = fieldList.get(column);
            ConcurrentHashMap<Object, List<Integer>> db = rowIndexDatabase.get(column);
            Object key = field.get(item);
            if (noIndex4Null && null == key) {
                continue;
            }
            db.putIfAbsent(key, new ArrayList<Integer>());
            List<Integer> columnIndexList = db.get(key);
            columnIndexList.add(row);
        }
    }


}
