package cc.xfl12345.mybigdata.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TmpTest {
    public static void main(String[] args) throws Exception {
        TestInitSlf4jAndLog4jOutsideWebApp.main(null);
        Logger logger = LogManager.getLogger(TmpTest.class);
    }
}
