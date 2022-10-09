package cc.xfl12345.mybigdata.server.web.pojo;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

public class RequestAnalyser {
    protected LinkedHashMap<String, IpAddressGetter> ipAddressGetters = new LinkedHashMap<>();

    public RequestAnalyser() {
        putDefaultIpAddressGetter(new DefaultIpAddressGetter("cf-connecting-ip"));
        putDefaultIpAddressGetter(new DefaultIpAddressGetter("X-Forwarded-For"){
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
        putDefaultIpAddressGetter(new DefaultIpAddressGetter("X-Real-IP"));
        putDefaultIpAddressGetter(new DefaultIpAddressGetter("REMOTE-HOST"));
    }

    private void putDefaultIpAddressGetter(DefaultIpAddressGetter getter) {
        ipAddressGetters.put(getter.getHeaderKey(), getter);
    }

    /**
     * 获取客户端真实IP地址
     *
     * @param request http请求
     * @return IP地址字符串
     */
    public String getIpAddress(HttpServletRequest request) {
        String ipAddr = null;

        for (IpAddressGetter getter : ipAddressGetters.values()) {
            ipAddr = getter.getIpAddress(request);
            if (ipAddr != null) {
                return ipAddr;
            }
        }

        return request.getRemoteAddr();
    }

}
