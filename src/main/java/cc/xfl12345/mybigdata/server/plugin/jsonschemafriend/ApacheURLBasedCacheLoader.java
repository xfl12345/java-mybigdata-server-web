package cc.xfl12345.mybigdata.server.plugin.jsonschemafriend;

import lombok.extern.slf4j.Slf4j;
import net.jimblackler.jsonschemafriend.CacheLoader;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ApacheURLBasedCacheLoader extends CacheLoader {
    private final FileSystemManager fileSystemManager;

    public ApacheURLBasedCacheLoader(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }

    public String load(URI uri, boolean cacheSchema) throws IOException {
        log.info("Loading JSON Schema file from: [" + uri + ']');
        FileObject fileObject = fileSystemManager.resolveFile(uri);
        if(cacheSchema) {
            fileSystemManager.getFilesCache().putFileIfAbsent(fileObject);
        }

        return fileObject.getContent().getString(StandardCharsets.UTF_8);
    }
}
