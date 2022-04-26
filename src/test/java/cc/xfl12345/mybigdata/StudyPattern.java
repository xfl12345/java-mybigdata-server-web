package cc.xfl12345.mybigdata;

import java.util.regex.Pattern;

public class StudyPattern {
    public static void main(String[] args) throws Exception {
        TestInitSlf4jAndLog4jOutsideWebApp.main(null);
        Pattern pattern = Pattern.compile("\\w+\\.json$");
        String[] strArr = {"零0", "1", "01",
            "012345", "0123abc", "abc0123",
            "ssdsad1231233dsd.jjjson",
            "ssdsad1231233dsd.jsonnn",
            "ssdsad1231233dsdjson",
            "ssdsad1231233dsd.123json",
            "ssdsad1231233dsd.json123",
            ". json",
            ".json ",
            "json",
            "json.",
            ".6json",
            ".json6",
            "..json",
            "6.json",
        };

        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i + ":" + pattern.matcher(strArr[i]).find());
        }

    }
}
