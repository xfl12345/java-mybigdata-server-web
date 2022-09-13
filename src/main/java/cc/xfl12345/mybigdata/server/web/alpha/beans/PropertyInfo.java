package cc.xfl12345.mybigdata.server.web.alpha.beans;

import cc.xfl12345.mybigdata.server.common.pojo.NameCasePack;
import lombok.Getter;

import java.lang.reflect.*;

public class PropertyInfo {
    @Getter
    protected Class<?> sourceClass;

    @Getter
    protected NameCasePack nameCasePack;

    @Getter
    protected Class<?> propertyType;
    protected Field propertyField = null;
    protected Method getter = null;
    protected Method setter = null;

    @Getter
    protected boolean readable = false;

    @Getter
    protected boolean writable = false;


    public PropertyInfo(Class<?> cls, String propertyName) throws IllegalArgumentException {
        this(cls, propertyName, true);
    }

    public PropertyInfo(Class<?> cls, String propertyName, boolean forceReadAndWrite) throws IllegalArgumentException {
        sourceClass = cls;
        nameCasePack = new NameCasePack(propertyName);
        // 尝试获取字段类型
        try {
            propertyField = sourceClass.getField(propertyName);
            propertyField.setAccessible(true);
            propertyType = propertyField.getType();
        } catch (NoSuchFieldException ignored) {
        }

        getter = tryGetGetter("get" + nameCasePack.studlyCase, propertyType);
        readable = getter != null;
        if (propertyType == null && getter != null) {
            propertyType = getter.getReturnType();
        }
        setter = tryGetSetter("set" + nameCasePack.studlyCase, propertyType);
        writable = setter != null;

        if (forceReadAndWrite) {
            if (!readable && !writable) {
                throw new IllegalArgumentException("Property [" + propertyName + "] is neither readable nor writable.");
            } else {
                if (!readable) {
                    throw new IllegalArgumentException("Property [" + propertyName + "] is not readable.");
                } else if (!writable) {
                    throw new IllegalArgumentException("Property [" + propertyName + "] is not writable.");
                }
            }
        }
    }


    protected Method tryGetGetter(String methodName, Class<?> returnType) {
        return tryGetGetter(sourceClass, methodName, returnType);
    }

    protected Method tryGetSetter(String methodName, Class<?>... parameterTypes) {
        return tryGetSetter(sourceClass, methodName, parameterTypes);
    }


    public static Method tryGetGetter(Class<?> cls, String methodName) {
        return tryGetGetter(cls, methodName, null);
    }

    public static Method tryGetSetter(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = cls.getMethod(methodName, parameterTypes);
            if ((method.getModifiers() & (Modifier.PROTECTED | Modifier.PRIVATE)) == 0) {
                method = null;
            }
        } catch (NoSuchMethodException ignored) {
        }
        return method;
    }

    public static Method tryGetGetter(Class<?> cls, String methodName, Class<?> returnType) {
        Method method = null;
        try {
            method = cls.getMethod(methodName, Void.class);
            if ((method.getModifiers() & (Modifier.PROTECTED | Modifier.PRIVATE)) == 0 ||
                returnType != null && !returnType.equals(method.getReturnType())) {
                method = null;
            }
        } catch (NoSuchMethodException ignored) {
        }
        return method;
    }


    protected Object read(Object srcPojo) throws Exception {
        if (readable) {
            if (getter == null) {
                return propertyField.get(srcPojo);
            }
            return getter.invoke(srcPojo);
        }
        return null;
    }

    protected void write(Object srcPojo, Object value) throws Exception {
        if (readable) {
            if (setter == null) {
                propertyField.set(srcPojo, value);
            }
            setter.invoke(srcPojo);
        }
    }


    public Object get(Object srcPojo) throws Exception {
        return read(srcPojo);
    }

    public void set(Object srcPojo, Object value) throws Exception {
        write(srcPojo, value);
    }
}
