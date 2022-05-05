package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.initializer.MockClassLoader;
import cc.xfl12345.mybigdata.server.plugin.apache.vfs.SpringBootResourceFileProvider;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@EnableConfigurationProperties
@SpringBootApplication
public class MybigdataApplication {
    private static MockClassLoader mockClassLoader;

    public static void main(String[] args) throws FileSystemException, MalformedURLException {

//        ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
//
//        mockClassLoader = new MockClassLoader(new URL[] {}, parentClassLoader);
//
//        String resRelatviePath = "META-INF/maven/com.google.jimfs/jimfs/pom.xml";
//        URL jimfsJarResourceURL = new UrlResource(Objects.requireNonNull(parentClassLoader.getResource(resRelatviePath))).getURL();
//
//        StandardFileSystemManager fileSystemManager = new StandardFileSystemManager();
//        fileSystemManager.init();
//        fileSystemManager.removeProvider("res");
//        fileSystemManager.addProvider("res", new SpringBootResourceFileProvider());
//        FileObject fileObject = fileSystemManager.resolveFile("res:/" + resRelatviePath);
//        URL jimfsLibJarFileURL = new URL(fileObject.getFileSystem().getRoot().getURL().getFile().toString());
//        mockClassLoader = new MockClassLoader(
//            new URL[] {jimfsLibJarFileURL},
//            parentClassLoader
//        );
//
//        Thread.currentThread().setContextClassLoader(mockClassLoader);

        SpringApplication.run(MybigdataApplication.class, args);
    }

}
