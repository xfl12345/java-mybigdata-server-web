package cc.xfl12345.mybigdata.server;

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class DemoJimFS {
    public static void main(String[] args) throws IOException {
        System.out.println(org.apache.commons.io.FileSystem.getCurrent().toString());
        // For a simple file system with Unix-style paths and behavior:
        FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());
        Path foo = fileSystem.getPath("/foo");
        Files.createDirectory(foo);
        System.out.println(fileSystem.getPath("/").toUri());

        Path hello = foo.resolve("hello.txt"); // /foo/hello.txt
        URL url = hello.toUri().toURL();
        System.out.println(url);
        Files.write(hello, ImmutableList.of("hello world"), StandardCharsets.UTF_8);
        InputStream inputStream = url.openStream();
        System.out.println(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
        inputStream.close();
    }
}
