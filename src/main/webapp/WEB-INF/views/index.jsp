<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.logging.log4j.Logger" %>
<%@ page import="org.apache.logging.log4j.LogManager" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.net.URL" %>
<%@ page import="com.alibaba.fastjson.JSONArray" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="cc.xfl12345.mybigdata.StaticSpringApp" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%
    //    Logger logger = LogManager.getLogger(getClass());
//    Date currentTime = new Date();
//    File file = new File(getClass().getClassLoader().getResource("").getFile());
//    logger.info(file.toURI().toString());
//
//    File targetDir = file.getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile();
//    File[] subFiles = targetDir.listFiles();
//    JSONObject jsonObject = new JSONObject();
//    if (subFiles != null) {
//        for (File subFile : subFiles) {
//            jsonObject.put(subFile.getName(), subFile.getPath());
//        }
//    }

    Logger logger = LogManager.getLogger(getClass());
    Date currentTime = new Date();
    Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("support/http/resources/");
    JSONArray jsonArray = new JSONArray();
    if (urls != null) {
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            jsonArray.add(url);
        }
    }


%>
<html>
<head>
    <title>欢迎使用</title>
</head>
<body>
<span>当前服务器时间：<%=StaticSpringApp.getSimpleDateFormat().format(currentTime) %></span><br/>
<div style="border: aqua dashed"><%=JSON.toJSONString(jsonArray, true) %>
</div>
</div>
<div style="border: aqua dashed"><%=IOUtils.toString(Thread.currentThread().getContextClassLoader()
    .getResource("support/http/resources/index.html").toURI(), StandardCharsets.UTF_8) %>
</div>
</body>
</html>
