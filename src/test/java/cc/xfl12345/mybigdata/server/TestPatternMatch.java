package cc.xfl12345.mybigdata.server;


import cc.xfl12345.mybigdata.server.model.utility.MyStrIsOK;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternMatch {
    public static void main(String[] args) {
        ArrayList<String> strArr = new ArrayList<>(Arrays.asList(
            "零0", "1", "01",
            "012345", "0123abc", "abc0123",
            "abc0123abc", "123abc0123", "helloworld",
            "九sss", "十emmm", "十一asdasdas...sdadasd",
            "十二asdasd12312312.1213asdasd", "十三...123asdas", "十四.",
            "十五[", "十六]\\", "十七///dsada",
            "十八ad12ed\\asd123231", "十九dasdaAA11e1312\\",
            ".0", "-.0", "+.0", "/1.0", "1.", "1..0",
            "01", "001", "01.0", "001.00", "000.00", "0..0",
            "-0", "-0.", "-00", "-01", "-01.00", "-1.0.0",
            "1.0", "-123.0", "+556875.0", "1.000", "1", "0.0"
        ));
        String password = "github@xfl12345";

        testPattern(strArr, MyStrIsOK.matchDigitOnly, "isOnlyDigit test");
        testPattern(strArr, MyStrIsOK.matchLetterOnly, "isOnlyLetter test");
        testPattern(strArr, MyStrIsOK.matchLetterAndDigitOnly, "isLetterDigit test");
        testPattern(strArr, MyStrIsOK.matchNumWithSignOnly, "matchNumWithSignOnly test");
        testPattern(strArr, MyStrIsOK.containNum, "isContainNum test");
        testPattern(strArr, MyStrIsOK.containUppercaseLetter, "isContainUppercaseLetter test");
        testPattern(strArr, MyStrIsOK.containLowercaseLetter, "isContainLowercaseLetter test");
        testPattern(strArr, MyStrIsOK.containAllowedSpecialCharacter, "isContainAllowedSpecialCharacter test");
        testPattern(strArr, MyStrIsOK.containChineseInUTF8, "isContainChineseInUTF8 test");
        testPattern(strArr, MyStrIsOK.matchEmailAddressOnly, "isEmailAddress test");


        System.out.println("removeNum test");
        for (int i = 0; i < strArr.size(); i++) {
            System.out.println(i + ":[" + MyStrIsOK.removeNum(strArr.get(i)) + ']');
        }

        System.out.println("removeLowercaseLetter test");
        for (int i = 0; i < strArr.size(); i++) {
            System.out.println(i + ":[" + MyStrIsOK.removeLowercaseLetter(strArr.get(i)) + ']');
        }

        System.out.println("removeLetter test");
        for (int i = 0; i < strArr.size(); i++) {
            System.out.println(i + ":[" + MyStrIsOK.removeLetter(strArr.get(i)) + ']');
        }

        System.out.println("test email string");
        System.out.println(MyStrIsOK.isEmailAddress("111@qq.com"));
    }


    public static void testPattern(List<String> contentArray, Pattern pattern, String title) {
        String foxLine = "======================";
        int titleLineLength = 0;
        if (title != null) {
            String titleLine = foxLine + ' ' + title + ' ' + foxLine;
            titleLineLength = titleLine.length();
            System.out.println(titleLine);
        }
        for (int i = 0; i < contentArray.size(); i++) {
            String content = contentArray.get(i);
            Matcher matcher = pattern.matcher(content);
            boolean isMatched = matcher.find();
            int resultGroupCount = isMatched ? matcher.groupCount() : 0;
            ArrayList<String> results = new ArrayList<>(resultGroupCount + 1);
            if (isMatched && resultGroupCount == 0) {
                results.add(matcher.group());
            } else {
                for (int j = 0; j < resultGroupCount; j++) {
                    results.add(j, matcher.group(j));
                }
            }

            System.out.println(i + ":" + isMatched
                + ", content:[" + content + "]"
                + ", group count:" + resultGroupCount
                + ", result:" + JSON.toJSONString(results, false));
        }
        if (title != null) {
            String stringBuilder = foxLine + foxLine +
                "=".repeat(Math.max(0, titleLineLength - foxLine.length() * 2));
            System.out.println(stringBuilder);
        }
    }
}
