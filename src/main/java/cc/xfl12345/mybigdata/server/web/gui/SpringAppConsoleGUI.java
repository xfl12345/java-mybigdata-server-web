package cc.xfl12345.mybigdata.server.web.gui;

import cc.xfl12345.mybigdata.server.web.initializer.EnvironmentOnCreatedInitializer;
import jp.uphy.javafx.console.ConsoleApplication;

public class SpringAppConsoleGUI extends ConsoleApplication {
    // @Override
    // public void beforeStart() throws Exception {
    //     super.beforeStart();
    //     setSafeByteTotalLimit(1000);
    // }

    @Override
    protected void invokeMain(String[] args) throws Exception {
        EnvironmentOnCreatedInitializer.waitGuiLock.justSynchronize();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
