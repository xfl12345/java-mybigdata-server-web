package cc.xfl12345.mybigdata.server.web.gui;

import cc.xfl12345.mybigdata.server.web.MybigdataApplication;
import jp.uphy.javafx.console.ConsoleApplication;

public class MybigdataWebServerGUI extends ConsoleApplication {
    @Override
    protected void invokeMain(String[] args) {
        try {
            MybigdataApplication.main(args);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
