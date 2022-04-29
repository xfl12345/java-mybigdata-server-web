package cc.xfl12345.mybigdata.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class StudyPattern {
    public static void main(String[] args) {
        // \w+(-[0-9]+)*\.json$}
        // \w+(?:-[0-9]+)*\.json$
        // "^\\w+(?:-\\w+)*\\.json$"
        // "^\\w+.+\\.json$"
        Pattern pattern = Pattern.compile("^\\w+.+\\.json$");
        ArrayList<String> contentArray = new ArrayList<>(Arrays.asList(
            "零0", "1", "01",
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
            "6.json", // should match
            "x-6.json", // should match
            "xx-6.json", // should match
            "x-66.json", // should match
            "xx-66.json", // should match
            ".6json",
            ".json6",
            "..json",
            "666..json",
            "x-.json",
            "xx--66.json",
            "xx-66..json",
            "weburi-/Gradle___cc_xfl12345___mybigdata_0_0_1_war__exploded_/index.json"
        ));
        TestPatternMatch.testPattern(contentArray, pattern, "alibaba druid json api url pattern test");
    }

}
