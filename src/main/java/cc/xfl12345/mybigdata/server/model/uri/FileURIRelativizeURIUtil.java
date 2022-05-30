package cc.xfl12345.mybigdata.server.model.uri;

import java.net.URI;

public class FileURIRelativizeURIUtil implements IRelativizeURIUtil {
    public String getRelativizeURI(URI base, URI child) {
        return base.relativize(child).getPath();
    }
}
