package cc.xfl12345.mybigdata.server.pojo;

import java.net.URL;
import java.util.concurrent.CopyOnWriteArraySet;

public class FileCacheInfoBean {
    protected URL originURL;
    protected CopyOnWriteArraySet<URL> cacheURLs;

    public FileCacheInfoBean() {
        cacheURLs = new CopyOnWriteArraySet<>();
    }

    public URL getOriginURL() {
        return originURL;
    }

    public void setOriginURL(URL originURL) {
        this.originURL = originURL;
    }

    public CopyOnWriteArraySet<URL> getCacheURLs() {
        return cacheURLs;
    }

    public boolean addCacheURL(URL url) {
        return cacheURLs.add(url);
    }
}
