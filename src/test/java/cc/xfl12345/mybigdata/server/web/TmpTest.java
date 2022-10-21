package cc.xfl12345.mybigdata.server.web;

import cc.xfl12345.mybigdata.server.common.database.pojo.CommonAccount;
import cc.xfl12345.mybigdata.server.common.utility.StringEscapeUtils;
import cn.hutool.core.bean.BeanUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class TmpTest {
    public static void main(String[] args) throws Exception {
        String content = "[君の名は。Fans] スパークル (original ver.) -Your name. Music Video edition- [DVDRip x264 N.mp4";
        System.out.println(StringEscapeUtils.escapeBracketsOnly4URL(content));
        // ClassLoader.getSystemResource("META-INF/resources/webjars/");

        System.out.println(BeanUtil.getPropertyDescriptorMap(CommonAccount.class, false));

        System.out.println("Arch:" + System.getProperty("os.arch"));

        System.out.println(new File(System.getenv("JAVA_HOME"), "bin").toPath());

        System.out.println("SystemEncoding: " + System.getProperty("sun.jnu.encoding"));
        Charset.forName(System.getProperty("sun.jnu.encoding"));

        try {
            Process notepadProcess = new ProcessBuilder("help").start();
            ProcessHandle myProcess = notepadProcess.toHandle().parent().get();
            System.out.println("Parent Process Native PID: "+ myProcess.parent().get().pid());
        } catch(IOException e) {
            e.printStackTrace();
        }



        // System.setOut();
    }
}
