package cc.xfl12345.mybigdata.server;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandlerFactory;

public class StudyURI {
    public static void main(String[] args) throws URISyntaxException, FileSystemException {
        StandardFileSystemManager fileSystemManager = (StandardFileSystemManager) VFS.getManager();
        URLStreamHandlerFactory factory = fileSystemManager.getURLStreamHandlerFactory();
        URL.setURLStreamHandlerFactory(factory);

        URI baseURI = fileSystemManager.resolveFile("https://asd.com/").getURI();
        URI anotherURI =fileSystemManager.resolveFile("https://asd.com/somestuff/another.html").getURI();

        String result = baseURI.relativize(anotherURI).getPath();

        System.out.println(result);
    }
}
