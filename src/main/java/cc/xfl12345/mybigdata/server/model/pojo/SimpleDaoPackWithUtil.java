package cc.xfl12345.mybigdata.server.model.pojo;

import java.util.HashMap;
import java.util.Map;

public class SimpleDaoPackWithUtil {
    private String tableName;
    private Disassembly daoDisassembly;
    private Disassembly poDisassembly;
    private Class<?> poBuilderClass;
    private final Map<String, Class<?>> classSimpleNames = new HashMap<>();
    private final Map<Class<?>, Object> instances = new HashMap<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Disassembly getDaoDisassembly() {
        return daoDisassembly;
    }

    public void setDaoDisassembly(Disassembly daoDisassembly) {
        replaceClassName(this.daoDisassembly, daoDisassembly);
        this.daoDisassembly = daoDisassembly;
    }

    public Disassembly getPoDisassembly() {
        return poDisassembly;
    }

    public void setPoDisassembly(Disassembly poDisassembly) {
        replaceClassName(this.poDisassembly, poDisassembly);
        this.poDisassembly = poDisassembly;
    }

    public Class<?> getPoBuilderClass() {
        return poBuilderClass;
    }

    public void setPoBuilderClass(Class<?> poBuilderClass) {
        replaceClassName(this.poBuilderClass, poBuilderClass);
        this.poBuilderClass = poBuilderClass;
    }

    public void replaceClassName(Class<?> old, Class<?> theNew) {
        if (old != null)
            classSimpleNames.remove(old.getSimpleName());
        if (theNew != null)
            classSimpleNames.put(theNew.getSimpleName(), theNew);
    }

    public void replaceClassName(Disassembly theOld, Disassembly theNew) {
        Class<?> theOldClass = theOld == null ? null : theOld.cls;
        Class<?> theNewClass = theNew == null ? null : theNew.cls;
        replaceClassName(theOldClass, theNewClass);
    }

    public Map<String, Class<?>> getClassSimpleNames() {
        return classSimpleNames;
    }

    public Map<Class<?>, Object> getInstances() {
        return instances;
    }

    public Object getInstance(Class<?> cls) {
        return instances.get(cls);
    }

    @SuppressWarnings("unchecked")
    public <T> T getTypedInstance(Class<T> cls) {
        return (T) instances.get(cls);
    }
}
