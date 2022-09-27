package cc.xfl12345.mybigdata.server.web.pojo;

import javax.servlet.http.HttpServletRequest;

public interface IpAddressGetter {
    String getIpAddress(HttpServletRequest request);
}
