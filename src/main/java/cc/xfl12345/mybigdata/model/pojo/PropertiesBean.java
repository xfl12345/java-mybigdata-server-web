package cc.xfl12345.mybigdata.model.pojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;


/**
 * 简单粗暴的把Properties文件加载成一个Properties对象
 */
public class PropertiesBean {
    protected Properties properties;

    public PropertiesBean(String resourcePath) throws IOException, URISyntaxException {
        URL url = getClass().getClassLoader().getResource(resourcePath);
        InputStream inputStream = new FileInputStream(new File(url.toURI()));
        properties = new Properties();
        properties.load(inputStream);
        inputStream.close();
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
