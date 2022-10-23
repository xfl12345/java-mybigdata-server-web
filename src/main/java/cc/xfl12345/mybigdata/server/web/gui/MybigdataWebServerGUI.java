package cc.xfl12345.mybigdata.server.web.gui;

import cc.xfl12345.mybigdata.server.web.MybigdataApplication;
import cc.xfl12345.mybigdata.server.web.appconst.EnvConst;
import jp.uphy.javafx.console.ConsoleApplication;
// import net.sourceforge.argparse4j.ArgumentParsers;
// import net.sourceforge.argparse4j.inf.ArgumentParser;
// import net.sourceforge.argparse4j.inf.ArgumentParserException;
// import net.sourceforge.argparse4j.inf.Namespace;

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

    public static void main(String[] args) throws Exception {
        // ArgumentParser parser = ArgumentParsers.newFor("MyBigData server").build().defaultHelp(true);
        // parser.addArgument("--app.gui.enabled")
        //     .dest("app.gui.enabled")
        //     .type(Boolean.class)
        //     .choices(Boolean.TRUE, Boolean.FALSE)
        //     .setDefault(Boolean.FALSE)
        //     .required(false)
        //     .help("Enable GUI or not. Default is 'false'.");
        // Namespace ns = null;
        // ns = parser.parseKnownArgs(args, null);
        // Boolean appGuiEnable = ns.getBoolean("app.gui.enabled");

        String appGuiEnable = System.getProperty(EnvConst.APP_GUI_ENABLED);
        if (appGuiEnable != null && !"".equals(appGuiEnable) && Boolean.parseBoolean(appGuiEnable)) {
            launch(args);
        } else {
            MybigdataApplication.main(args);
        }
    }
}
