package cc.xfl12345.mybigdata.server.web;

import org.apache.commons.vfs2.FileSystemException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.net.MalformedURLException;

@EnableConfigurationProperties
@SpringBootApplication
public class MybigdataApplication {
    public static void main(String[] args) throws FileSystemException, MalformedURLException {
        SpringApplication.run(MybigdataApplication.class, args);
    }

}
