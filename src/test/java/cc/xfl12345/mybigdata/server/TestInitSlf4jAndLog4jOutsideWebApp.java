package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.appconst.SpringAppLaunchMode;
import cc.xfl12345.mybigdata.server.initializer.InitLog4j2;

public class TestInitSlf4jAndLog4jOutsideWebApp {

    public static void main(String[] args) throws Exception {
        InitLog4j2.init(SpringAppLaunchMode.JAR);
    }
}
