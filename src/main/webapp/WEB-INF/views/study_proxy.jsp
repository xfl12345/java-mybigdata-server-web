<%--
  Created by IntelliJ IDEA.
  User: xfl666
  Date: 2022/4/24
  Time: 8:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.net.*,java.util.*,java.lang.*,java.io.*" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%

    String url = null;
    StringBuilder params = new StringBuilder();
    Enumeration<String> enu = request.getParameterNames();
//String str=request.getQueryString();
//System.out.println(str);
    int total = 0;
    while (enu.hasMoreElements()) {
        String paramName=(String)enu.nextElement();
        if(paramName.equals("url")){
            url=request.getParameter(paramName);
        }else{
            if(total==0){
                params.append(paramName).append("=").append(URLEncoder.encode(request.getParameter(paramName), StandardCharsets.UTF_8));
            } else {
                params.append("&").append(paramName).append("=").append(URLEncoder.encode(request.getParameter(paramName), StandardCharsets.UTF_8));
            }
            ++total;
        }
    }

    if(url != null){
        url = url + "?" + params.toString();
        System.out.println("url:"+url);
        // 使用GET方式向目的服务器发送请求
        URL connect = new URL(url.toString());
        URLConnection connection = connect.openConnection();
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while((line = reader.readLine()) != null){
            out.println(line);
        }
        reader.close();
    }
%>


