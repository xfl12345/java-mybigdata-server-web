package cc.xfl12345.mybigdata.server.utility;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class EncodeUtils {

    // charset名称 -> (Java 字符串 -> 转义字符串)
    public static final HashMap<String, HashMap<Character, byte[]>> mapper = new HashMap<>();
    public static final HashMap<String, HashMap<Character, String>> URLEncodeMapper = new HashMap<>();

    static {
        char[] charList = "()`~!@#$%^&*-_+=|{}[]:;'<>,.? /".toCharArray();
        Charset[] charsets = {
            StandardCharsets.UTF_8,
            StandardCharsets.ISO_8859_1,
            StandardCharsets.US_ASCII,
            StandardCharsets.UTF_16,
            StandardCharsets.UTF_16BE,
            StandardCharsets.UTF_16LE
        };

        for (Charset charset : charsets) {
            String charsetName = charset.name();
            HashMap<Character, byte[]> hashMap = new HashMap<>();
            HashMap<Character, String> hashMap4URL = new HashMap<>();
            for (Character ch : charList) {
                String charInString = "" + ch;
                hashMap.put(ch, charInString.getBytes(charset));
                hashMap4URL.put(ch, URLEncoder.encode(charInString, charset));
            }
            mapper.put(charsetName, hashMap);
            URLEncodeMapper.put(charsetName, hashMap4URL);
        }
    }

    public static String encodeBracketsOnly4URL(String content) {
        HashMap<Character, String> charMapper = URLEncodeMapper.get(StandardCharsets.ISO_8859_1.name());
        if (charMapper == null) {
            return content; // Not supported.
        }
        StringBuilder stringBuilder = new StringBuilder(content);
        int currentContentLength = content.length();
        for (int i = 0; i < currentContentLength; ) {
            char currChar = stringBuilder.charAt(i);
            switch (currChar) {
                case '[', ']', '{', '}' -> {
                    String replaceString = charMapper.get(currChar);
                    stringBuilder.deleteCharAt(i);
                    stringBuilder.insert(i, replaceString);
                    i += replaceString.length();
                    stringBuilder = new StringBuilder(stringBuilder);
                    currentContentLength = currentContentLength - 1 + replaceString.length();
                }
                default -> i++;
            }
        }
        return stringBuilder.toString();
    }
}
