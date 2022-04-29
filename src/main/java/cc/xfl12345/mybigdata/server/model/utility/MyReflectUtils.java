package cc.xfl12345.mybigdata.server.model.utility;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 这是一个通过Java反射机制获取变量的一个工具类
 * 方便获取类的字段名称，同时极大地减少字段以常量字符串形式写死在程序里
 */
public class MyReflectUtils {
    public static final HashMap<String, Class<?>> typeDic = new HashMap<>();

    static {
        typeDic.put(int.class.getName(), Integer.class);
        typeDic.put(double.class.getName(), Double.class);
        typeDic.put(float.class.getName(), Float.class);
        typeDic.put(long.class.getName(), Long.class);
        typeDic.put(short.class.getName(), Short.class);
        typeDic.put(byte.class.getName(), Byte.class);
        typeDic.put(boolean.class.getName(), Boolean.class);
        typeDic.put(char.class.getName(), Character.class);
    }

    /**
     * String 转 任意基本类型的包装类
     *
     * @param cls   基本类型
     * @param value String字符串
     * @return 基本类型的包装类的对象
     * @throws Exception 请务必保证目标类型有把String转成目标类型的构造方法
     */
    @SuppressWarnings("unchecked")
    public static <T> T castToWrapperClassObject(Class<T> cls, String value) throws Exception {
        Class<?> wrapperClass = typeDic.get(cls.getName());
        if (wrapperClass == null) {
            return cls.getConstructor(String.class).newInstance(value);
        }
        return (T) wrapperClass.getConstructor(String.class).newInstance(value);
    }

//    public static <T> Class<T> getGenericClass(Class<T> cls) throws ReflectiveOperationException {
//
//        return cls.getClass();
//    }

    public static Field getFieldByName(Object obj, String FieldName) {
        Field res = null;
        try {
            res = obj.getClass().getDeclaredField(FieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Map<String, Object> obj2Map(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    public static Object map2Obj(Map<String, Object> map, Class<?> cls) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object obj = cls.getDeclaredConstructor().newInstance();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }

    public static TreeSet<String> getFieldNamesAsTreeSet(Class<?> cls) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object obj = cls.getDeclaredConstructor().newInstance();
        TreeSet<String> treeSet = new TreeSet<String>();
        Field[] fields = cls.getDeclaredFields(); // 获取所有成员对象及函数
        for (Field f : fields) {
            f.setAccessible(true);// 暴力反射。 私有的也可以被访问。
            treeSet.add(f.getName());
        }
        return treeSet;
    }

    public static TreeSet<String> getFieldNamesAsArrayList(Class<?> cls) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Object obj = cls.getDeclaredConstructor().newInstance();
        TreeSet<String> treeSet = new TreeSet<String>();
        Field[] fields = cls.getDeclaredFields(); // 获取所有成员对象及函数
        for (Field f : fields) {
            f.setAccessible(true);// 暴力反射。 私有的也可以被访问。
            treeSet.add(f.getName());
        }
        return treeSet;
    }


    /*
     * 取得某一类所在包的所有类名 不含迭代
     */
    public static String[] getPackageAllClassName(String classLocation, String packageName) {
        //将packageName分解
        String[] packagePathSplit = packageName.split("[.]");
        String realClassLocation = classLocation;
        int packageLength = packagePathSplit.length;
        for (int i = 0; i < packageLength; i++) {
            realClassLocation = realClassLocation + File.separator + packagePathSplit[i];
        }
        File packeageDir = new File(realClassLocation);
        if (packeageDir.isDirectory()) {
            String[] allClassName = packeageDir.list();
            return allClassName;
        }
        return null;
    }

    /**
     * 从包package中获取所有的Class
     * source code URL=https://blog.csdn.net/jdzms23/article/details/17550119
     *
     * @param recursive 是否循环迭代
     */
    public static List<Class<?>> getClasses(String packageName, boolean recursive) throws IOException, ClassNotFoundException {
        //第一个class类的集合
        List<Class<?>> classes = new ArrayList<Class<?>>();
        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;

        dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        //循环迭代下去
        while (dirs.hasMoreElements()) {
            //获取下一个元素
            URL url = dirs.nextElement();
            //得到协议的名称
            String protocol = url.getProtocol();
            //如果是以文件的形式保存在服务器上
            if ("file".equals(protocol)) {
                //获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
                //以文件的方式扫描整个包下的文件 并添加到集合中
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
            } else if ("jar".equals(protocol)) {
                //如果是jar包文件
                //定义一个JarFile
                JarFile jar;
                //获取jar
                jar = ((JarURLConnection) url.openConnection()).getJarFile();
                //从此jar包 得到一个枚举类
                Enumeration<JarEntry> entries = jar.entries();
                //同样的进行循环迭代
                while (entries.hasMoreElements()) {
                    //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    //如果是以/开头的
                    if (name.charAt(0) == '/') {
                        //获取后面的字符串
                        name = name.substring(1);
                    }
                    //如果前半部分和定义的包名相同
                    if (name.startsWith(packageDirName)) {
                        int idx = name.lastIndexOf('/');
                        //如果以"/"结尾 是一个包
                        if (idx != -1) {
                            //获取包名 把"/"替换成"."
                            packageName = name.substring(0, idx).replace('/', '.');
                        }
                        //如果可以迭代下去 并且是一个包
                        if ((idx != -1) || recursive) {
                            //如果是一个.class文件 而且不是目录
                            if (name.endsWith(".class") && !entry.isDirectory()) {
                                //去掉后面的".class" 获取真正的类名
                                String className = name.substring(packageName.length() + 1, name.length() - 6);
                                //添加到classes
                                classes.add(Class.forName(packageName + '.' + className));
                            }
                        }
                    }
                }
            }
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     * source code URL=https://blog.csdn.net/jdzms23/article/details/17550119
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) {
        //获取此包的目录 建立一个File
        File dir = new File(packagePath);
        //如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        if (dirfiles == null) {
            return;
        }
        //循环所有文件
        for (File file : dirfiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                    file.getAbsolutePath(),
                    recursive,
                    classes);
            } else {
                //如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    //添加到集合中去
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static int demoFetch(Class<?> cls, Object obj) {
        //Field[] fields = cls.getFields(); // 获取所有公有的成员对象及函数
        Field[] fields = cls.getDeclaredFields(); // 获取所有成员对象及函数
        //  System.out.println(((Field)Arrays.stream(fields).toArray()[0]).getName());

        int count = 0;
        for (Field f : fields) {
            if (!f.canAccess(obj))
                f.setAccessible(true);// 暴力反射。 私有的也可以被访问。
            // System.out.println(f);
            try {
                System.out.println("成员名称:" + f.getName() +
                    " 成员修饰符: " + Modifier.toString(f.getModifiers()) +
                    " 成员数据类型: " + f.getGenericType().getTypeName() +
                    " 成员数据：" + f.get(obj));
                count++;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public static void demo(Class<?> cls) {
        Object obj;
        try {
            obj = cls.getDeclaredConstructor().newInstance();
            int count = demoFetch(cls, obj);
            System.out.println("共计 " + count + " 个成员");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void demo(Object obj) {
        try {
            Class<?> cls = obj.getClass();
            int count = demoFetch(cls, obj);
            System.out.println("共计 " + count + " 个成员");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
