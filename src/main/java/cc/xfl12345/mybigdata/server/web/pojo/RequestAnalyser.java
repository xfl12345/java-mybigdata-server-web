package cc.xfl12345.mybigdata.server.web.pojo;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

public class RequestAnalyser {
    protected LinkedHashMap<String, IpAddressGetter> ipAddressGetters = new LinkedHashMap<>();

    public RequestAnalyser() {
        putDefaultIpAddressGetter(new SimpleIpAddressGetter("cf-connecting-ip"));
        putDefaultIpAddressGetter(new SimpleIpAddressGetter("X-Forwarded-For"){
            @Override
            protected String getIpAddress(String headerContent) {
                String ipAddr = null;
                int index = headerContent.indexOf(',');
                if (index != -1) {
                    //只获取第一个值
                    ipAddr = headerContent.substring(0, index);
                } else {
                    ipAddr = headerContent;
                }

                return ipAddr;
            }
        });
        putDefaultIpAddressGetter(new SimpleIpAddressGetter("X-Real-IP"));
        putDefaultIpAddressGetter(new SimpleIpAddressGetter("REMOTE-HOST"));
    }

    private void putDefaultIpAddressGetter(SimpleIpAddressGetter getter) {
        ipAddressGetters.put(getter.getHeaderKey(), getter);
    }

    /**
     * 获取客户端真实IP地址
     *
     * @param request http请求
     * @return IP地址字符串
     */
    public String getIpAddress(HttpServletRequest request) {
        for (IpAddressGetter getter : ipAddressGetters.values()) {
            String ipAddr = getter.getIpAddress(request);
            if (ipAddr != null) {
                return ipAddr;
            }
        }

        return request.getRemoteAddr();
    }

}
