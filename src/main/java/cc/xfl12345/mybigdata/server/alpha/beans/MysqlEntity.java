package cc.xfl12345.mybigdata.server.alpha.beans;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

public class MysqlEntity {
    protected ReflectInfo reflectInfo;
    protected HashMap<String, PropertyInfo> propertyInfos;


    // protected LinkedHashSet<String>

    public MysqlEntity(Class<?> cls) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        reflectInfo = new ReflectInfo(cls);
        propertyInfos = new HashMap<>();
        Set<String> tmpNames;

        tmpNames = JSONObject.parseObject(
            JSON.toJSONString(
                cls.getDeclaredConstructor().newInstance(),
                JSONWriter.Feature.WriteMapNullValue
            ),
            JSONObject.class
        ).keySet();

        for (String propertyName : tmpNames) {
            try {
                propertyInfos.put(propertyName, new PropertyInfo(cls, propertyName, true));
            } catch (Exception ignored) {
            }
        }

        // TODO 实现一次性关联查询，查出两个表的所有字段内容

        // tmpNames = new HashSet<>(reflectInfo.methods.keySet());


    }

}
