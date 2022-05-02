package cc.xfl12345.mybigdata.server.pojo;

import cc.xfl12345.mybigdata.server.utility.MyReflectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PackageInfo {
    protected final HashMap<String, Disassembly> subClassMap = new HashMap<>();
    protected String name;
    protected boolean recursive;

    public PackageInfo(String name) throws IOException, ClassNotFoundException {
        this(name, false);
    }

    public PackageInfo(String name, boolean recursive) throws IOException, ClassNotFoundException {
        this.name = name;
        this.recursive = recursive;
        reInitMap(subClassMap, name, recursive);
    }

    public static void reInitMap(Map<String, Disassembly> map, String targetPackage, boolean recursive) throws IOException, ClassNotFoundException {
        map.clear();
        List<Class<?>> clsList = MyReflectUtils.getClasses(targetPackage, recursive);
        for (Class<?> item : clsList) {
            String classSimpleName = item.getSimpleName();
            map.put(classSimpleName, new Disassembly(item));
        }
    }

    public HashMap<String, Disassembly> getSubClassMap() {
        return subClassMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IOException, ClassNotFoundException {
        this.name = name;
        reInitMap(subClassMap, name, recursive);
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) throws IOException, ClassNotFoundException {
        this.recursive = recursive;
        reInitMap(subClassMap, name, recursive);
    }
}
