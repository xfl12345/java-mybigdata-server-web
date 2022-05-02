package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.appconst.SpringAppLaunchMode;
import cc.xfl12345.mybigdata.server.initializer.InitLog4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestLogFrameworkOutsideWebApp {

    public static void main(String[] args) throws Exception {
        InitLog4j2.init(SpringAppLaunchMode.JAR);
        Logger logger = LogManager.getLogger(TestLogFrameworkOutsideWebApp.class);
        logger.info("Just test.");
    }
}
