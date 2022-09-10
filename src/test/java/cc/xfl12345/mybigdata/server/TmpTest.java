package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.common.utility.StringEscapeUtils;

public class TmpTest {
    public static void main(String[] args) throws Exception {
        String content = "[君の名は。Fans] スパークル (original ver.) -Your name. Music Video edition- [DVDRip x264 N.mp4";
        System.out.println(StringEscapeUtils.escapeBracketsOnly4URL(content));

        System.out.println();
    }
}
