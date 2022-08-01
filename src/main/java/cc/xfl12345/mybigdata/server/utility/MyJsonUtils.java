package cc.xfl12345.mybigdata.server.utility;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

public class MyJsonUtils {

    /**
     * JSON对象 转 JSON字符串
     */
    public static String jsonObject2jsonStr(JSONObject jsonObject) {
        return jsonObject.toJSONString();
    }

    /**
     * JSON字符串 转 JSON对象
     */
    public static JSONObject jsonStr2jsonObject(String str) {
        return JSON.parseObject(str);
    }

    /**
     * JSON字符串 转 指定的Java对象
     */
    public static <T> T jsonStr2javaObject(String str, Class<T> cls) {
        return JSON.parseObject(str).to(cls);
    }

    /**
     * Java对象 转 JSON字符串
     */
    public static String javaObject2jsonStr(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * Java对象 转 JSON对象
     */
    public static JSONObject javaObject2jsonObject(Object object) {
        return (JSONObject) JSON.toJSON(object);
    }

    /**
     * JSON对象 转 指定的Java对象
     */
    public static <T> T jsonObject2javaObject(JSONObject jsonObject, Class<T> cls) {
        return JSON.to(cls, jsonObject);
    }


}
