package cc.xfl12345.mybigdata.server.initializer;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

public class MockClassLoader extends URLClassLoader {

    public MockClassLoader(URL[] urls) {
        super(urls);
    }

    public MockClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public MockClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    public MockClassLoader(String name, URL[] urls, ClassLoader parent) {
        super(name, urls, parent);
    }

    public MockClassLoader(String name, URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(name, urls, parent, factory);
    }

    public void addURL(URL url) {
        super.addURL(url);
    }
}
