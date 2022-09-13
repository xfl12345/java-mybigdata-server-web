package cc.xfl12345.mybigdata.server.web.model.uri;

import java.net.URI;
import java.net.URISyntaxException;

public interface URIRelativize {
    String getRelativizeURI(URI base, URI child) throws URISyntaxException;
}
