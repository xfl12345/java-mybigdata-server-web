package cc.xfl12345.mybigdata.server.web.alpha.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

public class ReflectInfo {
    protected Class<?> cls;

    protected LinkedHashMap<String, Field> fields;
    protected LinkedHashMap<String, Method> methods;

    protected LinkedHashMap<String, Field> declaredFields;
    protected LinkedHashMap<String, Method> declaredMethods;


    public ReflectInfo(Class<?> cls) {
        this.cls = cls;
        fields = new LinkedHashMap<>();
        methods = new LinkedHashMap<>();
        declaredFields = new LinkedHashMap<>();
        declaredMethods = new LinkedHashMap<>();

        Field[] tmpFields;
        Method[] tmpMethods;

        tmpFields = cls.getFields();
        for (Field field : tmpFields) {
            fields.put(field.getName(), field);
        }
        tmpMethods = cls.getMethods();
        for (Method method : tmpMethods) {
            methods.put(method.getName(), method);
        }

        tmpFields = cls.getDeclaredFields();
        for (Field field : tmpFields) {
            declaredFields.put(field.getName(), field);
        }
        tmpMethods = cls.getDeclaredMethods();
        for (Method method : tmpMethods) {
            declaredMethods.put(method.getName(), method);
        }
    }

}
