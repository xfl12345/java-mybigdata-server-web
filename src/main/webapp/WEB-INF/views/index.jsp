<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
<%@ page import="cc.xfl12345.mybigdata.server.model.StaticSpringApp" %>
<html>
<head>
    <title>欢迎使用</title>
</head>
<body>
    <span>当前服务器时间：<%=StaticSpringApp.getSimpleDateFormat().format(new Date()) %></span><br/>
</body>
</html>
