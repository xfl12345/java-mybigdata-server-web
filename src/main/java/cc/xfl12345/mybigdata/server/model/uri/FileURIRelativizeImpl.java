package cc.xfl12345.mybigdata.server.model.uri;

import java.net.URI;

public class FileURIRelativizeImpl implements URIRelativize {
    public String getRelativizeURI(URI base, URI child) {
        return base.relativize(child).getPath();
    }
}
