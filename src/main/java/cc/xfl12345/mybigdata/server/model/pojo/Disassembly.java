package cc.xfl12345.mybigdata.server.model.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Disassembly {
    public final Class<?> cls;
    public final LinkedHashMap<String, Field> fieldMap;
    public final TreeMap<String, Method> methodMap;

    public Disassembly(Class<?> cls) {
        this.cls = cls;
        fieldMap = new LinkedHashMap<>();
        methodMap = new TreeMap<>();

        addMember(fieldMap, cls.getDeclaredFields());
        addMember(methodMap, cls.getDeclaredMethods());
    }

    public static <T extends Member> void addMember(Map<String, T> memberMap, T[] members) {
        for (T member : members) {
            memberMap.put(member.getName(), member);
        }
    }
}
