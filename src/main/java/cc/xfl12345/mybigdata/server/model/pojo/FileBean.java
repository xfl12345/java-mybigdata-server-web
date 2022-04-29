package cc.xfl12345.mybigdata.server.model.pojo;

import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class FileBean {
    protected File file;

    public FileBean(Resource springResource) throws IOException {
        file = springResource.getFile();
    }

    public FileBean(String resourcePath) throws URISyntaxException {
        URL url = getClass().getClassLoader().getResource(resourcePath);
        file = new File(url.toURI());
    }

    public FileBean(String parent, String child) {
        file = new File(parent, child);
    }

    public FileBean(File parent, String child) {
        file = new File(parent, child);
    }

    public FileBean(URI uri) {
        file = new File(uri);
    }

    public FileBean() {
    }

    public FileBean(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
