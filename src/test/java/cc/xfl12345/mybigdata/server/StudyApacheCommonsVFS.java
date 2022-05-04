package cc.xfl12345.mybigdata.server;

import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class StudyApacheCommonsVFS {
    public static void main(String[] args) throws IOException {

        URL jsonSchemaSpecFilesRootDir = new URL("jar:file:/C:/Data/project/github/MyBigData_Java17/build/libs/mybigdata-0.0.1.jar!/BOOT-INF/classes/json/schema/");
        URL rootJsonSchemaURL = new URL("jar:file:/C:/Data/project/github/MyBigData_Java17/build/libs/mybigdata-0.0.1.jar!/BOOT-INF/classes/json/schema/spec/2020-12/schema.json");

        StandardFileSystemManager fileSystemManager = (StandardFileSystemManager) VFS.getManager();
        System.out.println(Arrays.toString(fileSystemManager.getSchemes()));
        URLStreamHandlerFactory factory = fileSystemManager.getURLStreamHandlerFactory();
        URL.setURLStreamHandlerFactory(factory); // VM global

        FileObject jarFileObject = fileSystemManager.resolveFile(jsonSchemaSpecFilesRootDir);
//        FileObject jarFileObject = fileSystemManager.resolveFile("res:/json/schema/spec/");
        FileObject[] children = jarFileObject.getChildren();
        System.out.println(children.length);

        FileObject ramFile = fileSystemManager.resolveFile("ram:/json/schema/");


        ramFile.copyFrom(jarFileObject, Selectors.SELECT_ALL);

//        Stream<String> stringStream = Arrays.stream(ramFile.getChildren()).map(fileObject -> {
//            try {
//                return fileObject.getURL().toString();
//            } catch (FileSystemException e) {
//                e.printStackTrace();
//            }
//            return null;
//        });
//        System.out.println(stringStream.toList());
//        stringStream.close();


        System.out.println(Arrays.toString(ramFile.resolveFile("spec/2020-12/meta/").getChildren()));

        URL url = ramFile.resolveFile("base_request_object.json").getURL();
        System.out.println(url);

        InputStream inputStream = url.openStream();
        String jsonFileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(jsonFileContent);
        inputStream.close();
    }
}
