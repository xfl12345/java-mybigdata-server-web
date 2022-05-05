package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.plugin.apache.vfs.SpringBootResourceFileProvider;
import cc.xfl12345.mybigdata.server.pojo.ResourceCacheMapBean;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.google.common.jimfs.Jimfs;
import com.google.common.jimfs.SystemJimfsFileSystemProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.VfsUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.spi.FileSystemProvider;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.SimpleTimeZone;

@Configuration
@Slf4j
public class AppConfig{
    @Bean("springbootExitCodeGenerator")
    public ExitCodeGenerator getSpringbootExitCodeGenerator() {
        return new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                return 0;
            }
        };
    }

    @Bean(name="defaultTimeZone")
    public SimpleTimeZone getDefaultTimeZone() {
        return new SimpleTimeZone(28800000, "China Standard Time");
    }

    @Bean(name="defaultDateFormat")
    public SimpleDateFormat getDefaultDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Bean(name="millisecondFormatter")
    public SimpleDateFormat getMillisecondFormatter() {
        return new SimpleDateFormat("SSS");
    }

    @Bean(name="fullDateFormat")
    public SimpleDateFormat getFullDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Bean(name="uuidGenerator")
    public TimeBasedGenerator getTimeBasedGenerator() {
        return Generators.timeBasedGenerator();
    }

    @Bean(name="jimFS")
    public FileSystem getJimFS() {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        URL jimfsJarResourceURL = new URL("jar:file:/C:/Data/project/github/MyBigData_Java17/build/libs/mybigdata-0.0.1.jar!/BOOT-INF/lib/jimfs-1.2.jar");
//        ServiceLoader<? extends FileSystemProvider> serviceLoader = ServiceLoader.load(SystemJimfsFileSystemProvider.class);
//        serviceLoader.reload();
        return Jimfs.newFileSystem(com.google.common.jimfs.Configuration.unix());
    }

    @Bean(name="resourceCacheMapBean")
    public ResourceCacheMapBean getResourceCacheBean() {
        return new ResourceCacheMapBean();
    }

    @Bean(name="apacheVfsFileSystemManager")
    public StandardFileSystemManager getStandardFileSystemManager() throws FileSystemException {
        StandardFileSystemManager fileSystemManager = (StandardFileSystemManager) VFS.getManager();
        fileSystemManager.removeProvider("res");
        fileSystemManager.addProvider("res", new SpringBootResourceFileProvider());
//        StandardFileSystemManager fileSystemManager = new StandardFileSystemManager();
//        fileSystemManager.setConfiguration(Thread.currentThread().getContextClassLoader()
//            .getResource("org/apache/commons/vfs2/impl/providers.xml"));
//        fileSystemManager.setCacheStrategy(CacheStrategy.MANUAL);
//        fileSystemManager.init();
//        URLStreamHandlerFactory factory = fileSystemManager.getURLStreamHandlerFactory();
//        URL.setURLStreamHandlerFactory(factory); // VM global
        log.info(fileSystemManager.getClass().getCanonicalName()
            + " is created.Support URL schema: "
            + Arrays.toString(fileSystemManager.getSchemes()));
        return fileSystemManager;
    }


}
