package cc.xfl12345.mybigdata.server;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.jar.JarFileObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Objects;

public class StudyURI {
    public static void main(String[] args) throws URISyntaxException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        ClassLoader currentSystemClassLoader = URLClassLoader.getSystemClassLoader();
        Field sclField = currentSystemClassLoader.getClass().getField("scl");
        sclField.setAccessible(true);


//        URL jimfsJarResourceURL = new URL("jar:file:/C:/Data/project/github/MyBigData_Java17/build/libs/mybigdata-0.0.1.jar!/BOOT-INF/lib/jimfs-1.2.jar");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL jimfsJarResourceURL = classLoader.getResource("META-INF/maven/com.google.jimfs/jimfs/pom.xml");
        InputStream inputStream = Objects.requireNonNull(jimfsJarResourceURL).openStream();
        System.out.println(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
        inputStream.close();


        StandardFileSystemManager fileSystemManager = (StandardFileSystemManager) VFS.getManager();
        FileObject fileObject = fileSystemManager.resolveFile(jimfsJarResourceURL);

        URL jimfsLibJarFileURL = fileObject.getFileSystem().getRoot().getURL();
        System.out.println(jimfsLibJarFileURL.toString());

        Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        if(method.canAccess(classLoader)) {
            method.invoke(classLoader, jimfsLibJarFileURL);
        }

//        String pathInString = Paths.get(rootJsonSchemaURL.toURI()).toString();

//        System.out.println(pathInString);
    }
}
