package cc.xfl12345.mybigdata.server.model;

import java.net.URI;
import java.net.URISyntaxException;

public interface IRelativizeURIUtil {
    String getRelativizeURI(URI base, URI child) throws URISyntaxException;
}
