package cc.xfl12345.mybigdata.server;

import lombok.SneakyThrows;
import org.apache.commons.vfs2.impl.DefaultFileSystemManager;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.zip.ZipFileSystem;
import org.apache.ibatis.io.Resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

public class StudyJarFile {
    public static void main(String[] args) throws IOException, URISyntaxException {
//        String sss = "C:\\Data\\project\\github\\MyBigData_Java17\\build\\libs\\mybigdata-0.0.1.jar\\BOOT-INF\\classes\\json\\schema\\";
//        System.out.println(new File(sss).toURI());

//        URL jsonSchemaSpecFilesRootDir = Resources.getResourceURL("json/schema/spec/2020-12/");
//        System.out.println(Resources.getResourceURL("json/schema/spec/2020-12/").toURI());
        URL jsonSchemaSpecFilesRootDir = new URL("jar:file:/C:/Data/project/github/MyBigData_Java17/build/libs/mybigdata-0.0.1.jar!/BOOT-INF/classes/json/schema/");
        FileSystem fileSystem = FileSystems.getFileSystem(jsonSchemaSpecFilesRootDir.toURI());

        JarURLConnection jarURLConnection = (JarURLConnection) jsonSchemaSpecFilesRootDir.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        String targetJarDirRelativePath = jsonSchemaSpecFilesRootDir.toString().substring(jsonSchemaSpecFilesRootDir.toString().indexOf("file:/") + jarURLConnection.getJarFileURL().toString().length());
        if(targetJarDirRelativePath.startsWith("!/")) {
            targetJarDirRelativePath = targetJarDirRelativePath.substring("!/".length());
        }


        HashMap<String, JarEntry> jarEntryHashMap = new HashMap<>();
        String finalTargetJarDirRelativePath = targetJarDirRelativePath;
        jarFile.stream().parallel().filter(
            jarEntry -> jarEntry.getName().startsWith(finalTargetJarDirRelativePath)
        ).forEach(
            jarEntry -> {
                System.out.println(jarEntry.getName());
                System.out.println(Arrays.toString(jarEntry.getName().split("/")));
                jarEntryHashMap.put(
                    jarEntry.getName(),
                    jarEntry
                );
            }
        );

        System.out.println(jarEntryHashMap);

    }


    private void copyJarResourcesFileToTemp(URI path, String tempPath, String filePrefix) {
        try {
            List<Map.Entry<ZipEntry, InputStream>> collect =
                readJarFile(new JarFile(path.getPath()), filePrefix)
                    .collect(Collectors.toList());
            for (Map.Entry<ZipEntry, InputStream> entry : collect) {
                // 文件相对路径
                String key = entry.getKey().getName();
                // 文件流
                InputStream stream = entry.getValue();
                File newFile = new File(tempPath + key.replaceAll("BOOT-INF/classes", ""));
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, newFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public static Stream<Map.Entry<ZipEntry, InputStream>> readJarFile(JarFile jarFile, String prefix) {
        Stream<Map.Entry<ZipEntry, InputStream>> readingStream =
            jarFile.stream()
                .filter(entry -> !entry.isDirectory() && entry.getName().startsWith(prefix))
                .map(entry -> {
                    try {
                        return new AbstractMap.SimpleEntry<>(entry, jarFile.getInputStream(entry));
                    } catch (IOException e) {
                        return new AbstractMap.SimpleEntry<>(entry, null);
                    }
                });
        return readingStream.onClose(() -> {
            try {
                jarFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
