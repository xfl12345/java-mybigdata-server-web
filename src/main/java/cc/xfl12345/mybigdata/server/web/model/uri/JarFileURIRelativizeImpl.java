package cc.xfl12345.mybigdata.server.web.model.uri;

import java.net.URI;
import java.net.URISyntaxException;

public class JarFileURIRelativizeImpl implements URIRelativize {
    public String getRelativizeURI(URI base, URI child) throws URISyntaxException {
        return (new URI(base.getRawSchemeSpecificPart())).relativize(new URI(child.getRawSchemeSpecificPart())).getPath();
    }
}
