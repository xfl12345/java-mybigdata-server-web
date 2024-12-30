package cc.xfl12345.mybigdata.server.web.interceptor;

import cc.xfl12345.mybigdata.server.web.pojo.RequestAnalyser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
public class DebugPrintRequestIpAddress implements HandlerInterceptor {

    private final RequestAnalyser requestAnalyser = new RequestAnalyser();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("IP address:  " + requestAnalyser.getIpAddress(request));
        return true;
    }
}
