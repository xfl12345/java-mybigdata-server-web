package cc.xfl12345.mybigdata.server.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

@Component
@Slf4j
public class ResourceCacheBean implements InitializingBean {
    @Getter
    @Setter
    protected FileSystem memoryFileSystem;

    @Getter
    @Setter
    protected ClassLoader classLoader;

    @Getter
    @Setter
    protected String resourcePath;

    protected ReentrantReadWriteLock readWriteLock;
    protected HashMap<String, URL> resourceMap;

    protected URL resourceRootURL;
    protected URI resourceRootURI;

    public ResourceCacheBean() {
        readWriteLock = new ReentrantReadWriteLock();
        resourceMap = new HashMap<>();
    }

    protected void setResourceRootURL(URL url) throws URISyntaxException {
        resourceRootURL = url;
        resourceRootURI = url.toURI();
    }

    public URL getResourceRootURL() {
        return resourceRootURL;
    }

    public URL getResourceURL(String resourcePath) {
        if (resourcePath == null) {
            resourcePath = "/";
        }
        if (resourcePath.charAt(0) != '/') {
            resourcePath = '/' + resourcePath;
        }

//        FileSystems.getFileSystem()
        readWriteLock.readLock().lock();
        URL result = resourceMap.get(resourcePath);
        readWriteLock.readLock().unlock();
        return result;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }

        URL tempURL = classLoader.getResource(resourcePath);
        URI tempURI = Objects.requireNonNull(tempURL).toURI();
        String tempUrlInString = tempURL.toString();
        readWriteLock.writeLock().lock();
        switch (Objects.requireNonNull(tempURL).getProtocol()) {
            case "file" -> {
                setResourceRootURL(tempURL);
            }
            case "jar" -> {
                JarURLConnection jarURLConnection = (JarURLConnection) tempURL.openConnection();
                JarFile jarFile = jarURLConnection.getJarFile();
                URL jarFileURL = jarURLConnection.getJarFileURL();
                String jarFileUrlInString = jarFileURL.toString();
                StringBuilder relativePathBuilder = new StringBuilder(
                    tempUrlInString.substring(
                        tempUrlInString.indexOf("file:/") + jarFileUrlInString.length()
                    )
                );
                if (relativePathBuilder.charAt(0) == '!') {
                    if (relativePathBuilder.charAt(1) == '/') {
                        relativePathBuilder.deleteCharAt(0);
                    } else {
                        relativePathBuilder.setCharAt(0, '/');
                    }
                }
                if (relativePathBuilder.charAt(0) != '/') {
                    relativePathBuilder.insert(0, '/');
                }
                final String targetJarDirRelativePath = relativePathBuilder.toString();
                Stream<JarEntry> jarEntryStream = jarFile.stream().parallel().filter(
                    jarEntry -> jarEntry.getName().startsWith(targetJarDirRelativePath)
                );
                Iterator<JarEntry> jarEntries = jarEntryStream.iterator();
                while (jarEntries.hasNext()) {
                    JarEntry jarEntry = jarEntries.next();
                    // 获取 JAR 包内的路径
                    String path2entry = '/' + jarEntry.getName();
                    // 获取 文件系统 内的路径
                    Path pathInFS = memoryFileSystem.getPath(path2entry);
                    // 即使是文件夹，也要被缓存
                    if (jarEntry.isDirectory()) {
                        // 文件夹若不存在则创建
                        Files.createDirectories(pathInFS);
                    } else {
                        // 文件的根目录若不存在则创建
                        Files.createDirectories(pathInFS.getParent());
                        // 获取文件输入流
                        InputStream inputStream = jarFile.getInputStream(jarEntry);
                        // 写到 文件系统 里
                        Files.write(pathInFS, inputStream.readAllBytes());
                        // 常规操作，关闭文件输入流
                        inputStream.close();
                    }
                    // 获取并缓存 URL
                    resourceMap.putIfAbsent(
                        path2entry,
                        memoryFileSystem.getPath(path2entry).toUri().toURL()
                    );
                }
                jarFile.close();
            }
            default -> {
                readWriteLock.writeLock().unlock();
                throw new NotImplementedException();
            }
        }

        readWriteLock.writeLock().unlock();
    }
}
