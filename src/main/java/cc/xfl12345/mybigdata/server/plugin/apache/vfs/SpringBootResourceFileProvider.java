package cc.xfl12345.mybigdata.server.plugin.apache.vfs;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.res.ResourceFileProvider;
import org.apache.commons.vfs2.provider.res.ResourceFileSystemConfigBuilder;
import lombok.NonNull;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * source code URL=https://github.com/ManyDesigns/Portofino/blob/master/microservices/spring-boot/src/main/java/com/manydesigns/portofino/microservices/boot/SpringBootResourceFileProvider.java
 */
public class SpringBootResourceFileProvider extends ResourceFileProvider {
    protected static final Pattern matchJarURI = Pattern.compile("[.]jar!");

    @Override
    public FileObject findFile(final FileObject baseFile, final String uri, final FileSystemOptions fileSystemOptions)
        throws FileSystemException {
        final FileName fileName;
        if (baseFile != null) {
            fileName = parseUri(baseFile.getName(), uri);
        }
        else {
            fileName = parseUri(null, uri);
        }
        final String resourceName = fileName.getPath();

        ClassLoader classLoader = ResourceFileSystemConfigBuilder.getInstance().getClassLoader(fileSystemOptions);
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        FileSystemException.requireNonNull(classLoader, "vfs.provider.url/badly-formed-uri.error", uri);
        final URL url = classLoader.getResource(resourceName);

        FileSystemException.requireNonNull(url, "vfs.provider.url/badly-formed-uri.error", uri);

        return getContext().getFileSystemManager().resolveFile(fixNestedURI(url.toExternalForm()));
    }

    //See https://github.com/bedatadriven/renjin/blob/cac412d232ad66d4ee8e37cfc8cb70a45e676e19/core/src/main/java/org/renjin/util/ClasspathFileProvider.java#L88-L126
    public String fixNestedURI(@NonNull String uri) {
        int bang = uri.indexOf('!');
        if(bang < 0 || !uri.startsWith("jar:file:")) {
            return uri;
        } else {
            StringBuilder prefix = new StringBuilder();
            Matcher matcher = matchJarURI.matcher(uri);
            if(matcher.find()) {
                while (matcher.find()) {
                    prefix.append("jar:");
                }
            }
            matcher = Pattern.compile("([^.][^j][^a][^r])!").matcher(uri);
            return prefix + matcher.replaceAll("$1");
        }
    }
}
