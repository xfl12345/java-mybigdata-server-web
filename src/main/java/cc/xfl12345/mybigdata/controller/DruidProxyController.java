//package cc.xfl12345.mybigdata.controller;
//
//import com.alibaba.druid.support.http.StatViewServlet;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.Servlet;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Map;
//
//@Controller
//@Slf4j
//@RequestMapping(DruidProxyController.filterPatternContent)
//public class DruidProxyController implements ApplicationContextAware {
//    public static final String forwardServletName = "druid-local";
//    public static final String forwardServletPath = "/" + forwardServletName;
//    public static final String forwardServletPathCache1 = forwardServletPath + "/";
//    public static final String filterPatternContent = "druid-proxy";
//    public static final String filterPatternContentCache1 = "/" + filterPatternContent;
//    public static final String filterPatternContentCache2 = filterPatternContentCache1 + "/";
//    public static final String filterPatternContentCache3 = filterPatternContentCache1 + "/login.html";
//    public static final String filterPatternContentCache4 = filterPatternContentCache1 + "/index.html";
//
//    protected ApplicationContext applicationContext;
//    protected StatViewServlet statViewServlet;
//
//    @Autowired
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//
//        String forwardServletPatternValue = forwardServletPath + "/*";
//        for (String servletBeanName : applicationContext.getBeanNamesForType(ServletRegistrationBean.class)) {
//            ServletRegistrationBean servletBean = applicationContext.getBean(servletBeanName, ServletRegistrationBean.class);
//            if (servletBean.getUrlMappings().contains(forwardServletPatternValue)) {
//                Servlet servlet = servletBean.getServlet();
//                if (servlet instanceof StatViewServlet) {
//                    statViewServlet = (StatViewServlet) servlet;
//                    break;
//                }
//            }
//        }
//    }
//
//    @RequestMapping(path = {"", "/", "index"})
//    public String index1(HttpServletRequest request) {
//
//        return statViewServlet.isRequireAuth() ?
//            "redirect:" + filterPatternContentCache4 :
//            "redirect:" + filterPatternContentCache3;
//    }
//
//    @RequestMapping(path = {"/**"})
//    public void any(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//
//        String targetPath = request.getServletPath().substring(filterPatternContentCache1.length());
//        if ("/submitLogin".equals(targetPath)) {
//            Map<String, String[]> paramterMap = request.getParameterMap();
////            paramterMap.replace(PARAM_NAME_USERNAME, "root");
//        }
//        String forwardPath = forwardServletPath + targetPath;
//        request.getRequestDispatcher(forwardPath).forward(request, response);
//    }
//}
