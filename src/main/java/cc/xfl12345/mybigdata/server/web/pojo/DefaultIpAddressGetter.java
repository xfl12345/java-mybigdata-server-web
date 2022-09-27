package cc.xfl12345.mybigdata.server.web.pojo;

import javax.servlet.http.HttpServletRequest;

public class DefaultIpAddressGetter implements IpAddressGetter {
    protected String headerKey;

    public String getHeaderKey() {
        return headerKey;
    }

    public DefaultIpAddressGetter(String headerKey) {
        this.headerKey = headerKey;
    }

    @Override
    public String getIpAddress(HttpServletRequest request) {
        String headerContent = request.getHeader("X-Real-IP");
        if (headerContent != null && !"".equals(headerContent) && !"unknown".equalsIgnoreCase(headerContent)) {
            return headerContent;
        }

        return null;
    }
}
