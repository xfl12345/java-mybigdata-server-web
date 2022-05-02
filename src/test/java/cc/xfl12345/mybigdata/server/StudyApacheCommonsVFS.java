package cc.xfl12345.mybigdata.server;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;

import java.net.MalformedURLException;
import java.net.URL;

public class StudyApacheCommonsVFS {
    public static void main(String[] args) throws FileSystemException, MalformedURLException {

        URL jsonSchemaSpecFilesRootDir = new URL("jar:file:/C:/Data/project/github/MyBigData_Java17/build/libs/mybigdata-0.0.1.jar!/BOOT-INF/classes/json/schema/");

        StandardFileSystemManager fileSystemManager = (StandardFileSystemManager) VFS.getManager();
        FileObject fileObject = fileSystemManager.resolveFile(jsonSchemaSpecFilesRootDir);
        FileObject[] children = fileObject.getChildren();

        System.out.println(children.length);
    }
}
