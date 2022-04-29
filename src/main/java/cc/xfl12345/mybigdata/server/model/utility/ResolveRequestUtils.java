/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cc.xfl12345.mybigdata.server.model.utility;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

/**
 * 这是一个从http请求里提取数据的工具类
 * source code URL=https://blog.csdn.net/weixin_34226182/article/details/89551738
 */
public class ResolveRequestUtils {
    protected final static HashMap<String, Charset> standardCharsetDictionary = new HashMap<>();

    static {
        Arrays.stream(StandardCharsets.class.getDeclaredFields()).forEach(
            theField -> {
                if (Charset.class.isAssignableFrom(theField.getType()) && theField.canAccess(null)) {
                    Charset charset = null;
                    try {
                        charset = (Charset) theField.get(null);
                        standardCharsetDictionary.put(charset.name(), charset);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
    }

    /**
     * 这是一个从http请求里获取 com.alibaba.fastjson.JSONObject 的main方法
     */
    public static JSONObject getJsonObject(HttpServletRequest request) throws IOException {
        return getJsonObject(getRequestString(request));
    }

    public static JSONObject getJsonObject(String jsonString) throws IOException {
        return JSONObject.parseObject(jsonString);
    }

    /**
     * 以字符串的形式，获取 request 中 请求体 的内容，不管是post还是get请求，万能
     * source code URL https://blog.csdn.net/qq_28863045/article/details/79503945
     */
    public static String getRequestString(HttpServletRequest request)
        throws IOException {
        String submitMehtod = request.getMethod().toUpperCase(Locale.ROOT);
        // GET
        if (submitMehtod.equals("GET")) {
            return getRequestStrFromGET(request);
        } else {// POST
            return getRequestStrFromPOST(request);
        }
    }

    /**
     * 获取请求数据的编码格式
     *
     * @param request 一个http请求
     * @return 以字符串形式返回http请求的编码格式
     */
    public static String getRequestCharEncoding(HttpServletRequest request) {
        String charEncoding = request.getCharacterEncoding();
        //如果请求数据包没有定义（注明）编码，默认使用 iso-8859-1
        if (charEncoding == null) {
            charEncoding = StandardCharsets.ISO_8859_1.name();
        }
        return charEncoding;
    }

    /**
     * 以字符串的形式，获取 post 请求内容
     */
    public static String getRequestStrFromPOST(HttpServletRequest request) throws IOException {
        byte[] buffer = getRequestPostBytes(request);
        if (buffer == null) {
            return "";
        }
        String charEncodingName = getRequestCharEncoding(request);
        Charset charEncoding = standardCharsetDictionary.get(charEncodingName);
        return charEncoding == null ? new String(buffer, charEncodingName) : new String(buffer, charEncoding);
    }

    /**
     * 以字符串的形式，获取 get 请求内容
     * source code URL=https://blog.csdn.net/qq_28863045/article/details/79503945
     */
    public static String getRequestStrFromGET(HttpServletRequest request) throws IOException {
        String rawData = request.getQueryString();
        String charEncodingName = getRequestCharEncoding(request);
        Charset charEncoding = standardCharsetDictionary.get(charEncodingName);
        if (rawData != null) {
            byte[] rawDataBytes = charEncoding == null ? rawData.getBytes(charEncodingName) : rawData.getBytes(charEncoding);
            return new String(rawDataBytes, StandardCharsets.UTF_8).replaceAll("%22", "\"");
        }
        return null;
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request) {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        try {
            ServletInputStream servletInputStream = request.getInputStream();
            //尝试读取数据，尽量读完整个InputStream
            for (int i = 0; i < contentLength; ) {
                int readlen = servletInputStream.read(buffer, i, contentLength - i);
                if (readlen == -1) {
                    break;
                }
                i += readlen;
            }
            servletInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 将JSON对象以字符串的形式响应给客户端
     *
     * @param response   http响应对象
     * @param jsonObject alibaba fastjson的JSONObject对象
     */
    public static void responseJsonStr(HttpServletResponse response, JSONObject jsonObject) {
        if (response != null) {
            response.setContentType("application/json;charset=utf-8");
            try (PrintWriter out = response.getWriter()) {
                out.append(jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将JSON对象以字符串的形式响应给客户端
     *
     * @param response http响应对象
     * @param object   任意Java对象
     */
    public static void responseObjectAsJsonStr(HttpServletResponse response, Object object) {
        if (response != null) {
            response.setContentType("application/json;charset=utf-8");
            try (PrintWriter out = response.getWriter()) {
                out.append(((JSONObject) JSONObject.toJSON(object)).toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
