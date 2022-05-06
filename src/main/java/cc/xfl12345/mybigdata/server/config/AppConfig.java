package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.plugin.apache.vfs.SpringBootResourceFileProvider;
import cc.xfl12345.mybigdata.server.pojo.ResourceCacheMapBean;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.CacheStrategy;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ram.RamFileObject;
import org.apache.commons.vfs2.provider.ram.RamFileProvider;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.SimpleTimeZone;

@Configuration
@Slf4j
public class AppConfig {
    @Bean("springbootExitCodeGenerator")
    public ExitCodeGenerator getSpringbootExitCodeGenerator() {
        return new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                return 0;
            }
        };
    }

    @Bean(name = "defaultTimeZone")
    public SimpleTimeZone getDefaultTimeZone() {
        return new SimpleTimeZone(28800000, "China Standard Time");
    }

    @Bean(name = "defaultDateFormat")
    public SimpleDateFormat getDefaultDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Bean(name = "millisecondFormatter")
    public SimpleDateFormat getMillisecondFormatter() {
        return new SimpleDateFormat("SSS");
    }

    @Bean(name = "fullDateFormat")
    public SimpleDateFormat getFullDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Bean(name = "uuidGenerator")
    public TimeBasedGenerator getTimeBasedGenerator() {
        return Generators.timeBasedGenerator();
    }

    @Bean(name = "resourceCacheMapBean")
    public ResourceCacheMapBean getResourceCacheBean() {
        return new ResourceCacheMapBean();
    }

    @Bean(name = "apacheVfsFileSystemManager")
    public StandardFileSystemManager getStandardFileSystemManager() throws IOException {
        SpringBootResourceFileProvider fileProvider = new SpringBootResourceFileProvider();

        URL confURL =  Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                .getResource("org/apache/commons/vfs2/impl/providers.xml"));

        DefaultFileSystemManager tmpFileSystemManager = new DefaultFileSystemManager();
        tmpFileSystemManager.addProvider("ram", new RamFileProvider());
        tmpFileSystemManager.setCacheStrategy(CacheStrategy.MANUAL);
        tmpFileSystemManager.init();
        RamFileObject ramFileObject = (RamFileObject) tmpFileSystemManager.resolveFile("ram:/"
            + "org/apache/commons/vfs2/impl/providers.xml");
        InputStream inputStream = confURL.openStream();
        String xmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        inputStream.close();
        inputStream = IOUtils.toInputStream(xmlContent, StandardCharsets.UTF_8);
        OutputStream outputStream = ramFileObject.getOutputStream();
        outputStream.write(inputStream.readAllBytes());
        outputStream.close();
        inputStream.close();


        StandardFileSystemManager fileSystemManager = new StandardFileSystemManager();
        fileSystemManager.setConfiguration(Objects.requireNonNull(ramFileObject.getURL()));
        fileSystemManager.setCacheStrategy(CacheStrategy.MANUAL);
        fileSystemManager.init();
        fileSystemManager.removeProvider("res");
        fileSystemManager.addProvider("res", fileProvider);
        VFS.setManager(fileSystemManager);
        log.info(fileSystemManager.getClass().getCanonicalName()
            + " is created.Support URL schema: "
            + Arrays.toString(fileSystemManager.getSchemes()));
//        tmpFileSystemManager.close();
        return fileSystemManager;
    }


}
