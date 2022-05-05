package cc.xfl12345.mybigdata.server.model;

import java.net.URI;
import java.net.URISyntaxException;

public class JarFileURIRelativizeURIUtil implements IRelativizeURIUtil {
    public String getRelativizeURI(URI base, URI child) throws URISyntaxException {
        return (new URI(base.getRawSchemeSpecificPart())).relativize(new URI(child.getRawSchemeSpecificPart())).getPath();
    }
}
