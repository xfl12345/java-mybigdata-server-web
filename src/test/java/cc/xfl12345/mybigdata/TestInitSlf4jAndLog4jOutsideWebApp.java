package cc.xfl12345.mybigdata;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TestInitSlf4jAndLog4jOutsideWebApp {

    public static void main(String[] args) throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("log4j2.xml");
        //假装认为 代码块结束之后，string应该会被回收
        {
            Document xmlDoc = Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), "", Parser.xmlParser());
            inputStream.close();
            xmlDoc.getElementsByTag("configuration").get(0)
                    .getElementsByTag("Properties").get(0)
                    .getElementsByAttributeValue("name", "logBaseFolder")
                    .html("${sys:user.home}/logs/${APPNAME}");
            inputStream = IOUtils.toInputStream(xmlDoc.html(), StandardCharsets.UTF_8);
        }
        ConfigurationSource source = new ConfigurationSource(inputStream);
        Configurator.initialize(null, source);
        inputStream.close();
        Logger logger = LogManager.getLogger(TestInitSlf4jAndLog4jOutsideWebApp.class);
        logger.debug("Test logger output.");
    }
}
