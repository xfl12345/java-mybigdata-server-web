package cc.xfl12345.mybigdata.server.config;

import cc.xfl12345.mybigdata.server.plugin.apache.vfs.SpringBootResourceFileProvider;
import cc.xfl12345.mybigdata.server.pojo.ResourceCacheMapBean;
import cc.xfl12345.mybigdata.server.service.VfsWebDavService;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.CacheStrategy;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.ftps.FtpsFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.ram.RamFileObject;
import org.apache.commons.vfs2.provider.ram.RamFileProvider;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.webdav4.Webdav4FileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.zip.ZipFileSystemConfigBuilder;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
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

    @Bean(name = "fileSystemOptions")
    public FileSystemOptions getFileSystemOptions() throws FileSystemException {
        FileSystemOptions fileSystemOptions = new FileSystemOptions();

        ZipFileSystemConfigBuilder zipBuilder = ZipFileSystemConfigBuilder.getInstance();
        zipBuilder.setCharset(fileSystemOptions, StandardCharsets.UTF_8);

        FtpsFileSystemConfigBuilder ftpsBuilder = FtpsFileSystemConfigBuilder.getInstance();
        ftpsBuilder.setConnectTimeout(fileSystemOptions, Duration.ofSeconds(5));
        ftpsBuilder.setUserDirIsRoot(fileSystemOptions, false);
        ftpsBuilder.setAutodetectUtf8(fileSystemOptions, true);
        ftpsBuilder.setPassiveMode(fileSystemOptions, true);
        ftpsBuilder.setControlEncoding(fileSystemOptions, StandardCharsets.UTF_8.name());

        SftpFileSystemConfigBuilder sftpBuilder = SftpFileSystemConfigBuilder.getInstance();
        sftpBuilder.setConnectTimeout(fileSystemOptions, Duration.ofSeconds(5));
        sftpBuilder.setUserDirIsRoot(fileSystemOptions, false);
        sftpBuilder.setStrictHostKeyChecking(fileSystemOptions, "no");

        Webdav4FileSystemConfigBuilder webdavBuilder = Webdav4FileSystemConfigBuilder.getInstance();
        webdavBuilder.setConnectionTimeout(fileSystemOptions, Duration.ofSeconds(50));
        webdavBuilder.setSoTimeout(fileSystemOptions, Duration.ofSeconds(50));
        webdavBuilder.setMaxConnectionsPerHost(fileSystemOptions, 1000);
        webdavBuilder.setMaxTotalConnections(fileSystemOptions, 1000);
        webdavBuilder.setHostnameVerificationEnabled(fileSystemOptions, false);
        webdavBuilder.setPreemptiveAuth(fileSystemOptions, true);
        webdavBuilder.setUrlCharset(fileSystemOptions, StandardCharsets.UTF_8.name());
        webdavBuilder.setFollowRedirect(fileSystemOptions, true);
        webdavBuilder.setKeepAlive(fileSystemOptions, true);

        return fileSystemOptions;
    }

    @Bean(name = "apacheVfsFileSystemManager")
    public StandardFileSystemManager getStandardFileSystemManager() throws IOException {
        // 修复一些BUG，让SpringBoot APP哪怕以JAR包运行也能正常读取resource资源
        SpringBootResourceFileProvider resourceFileProvider = new SpringBootResourceFileProvider();

        // 获取 "org/apache/commons/vfs2/impl/providers.xml" 资源
        URL confURL = Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
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


        // 正式开始实例化主角
        StandardFileSystemManager fileSystemManager = new StandardFileSystemManager();
        fileSystemManager.setConfiguration(Objects.requireNonNull(ramFileObject.getURL()));
        fileSystemManager.setCacheStrategy(CacheStrategy.MANUAL);
        fileSystemManager.init();
        fileSystemManager.removeProvider("res");
        fileSystemManager.addProvider("res", resourceFileProvider);
        VFS.setManager(fileSystemManager);
        log.info(fileSystemManager.getClass().getCanonicalName()
            + " is created.Support URL schema: "
            + Arrays.toString(fileSystemManager.getSchemes()));
//        tmpFileSystemManager.close();
        return fileSystemManager;
    }

    @Bean(name = "vfsWebDavService")
    @ConfigurationProperties(prefix = "service.vfs-webdav")
    public VfsWebDavService getVfsWebDavService() throws IOException {
        VfsWebDavService vfsWebDavService = new VfsWebDavService();
        vfsWebDavService.setFileSystemManager(getStandardFileSystemManager());
        vfsWebDavService.setFileSystemOptions(getFileSystemOptions());
        return vfsWebDavService;
    }


}
