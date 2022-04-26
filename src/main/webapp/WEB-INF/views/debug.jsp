<%--
  Created by IntelliJ IDEA.
  User: xfl666
  Date: 2021/5/7
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Title</title>
    <link href="<%=request.getContextPath() %>/static/css/login_common.css" rel="stylesheet" type="text/css"/>
</head>
<style>
    input, textarea {
        width: 100%;
    }
</style>
<body>

<div id="jsInsert" class="jsInsert" style="border-style: solid;word-wrap: break-word">
</div>
<br>
<br>
<div id="formTable">
    <div>requestMethod:<br>
        <label for="requestMethod">
            <input id="requestMethod" class="jsInsert" type="text"/>
        </label>
    </div>
    <div>requestURL:<br>
        <label for="requestURL">
            <input id="requestURL" class="jsInsert" type="text"/>
        </label>
    </div>
    <br>
    <br>
    <div>requsetInput:<br>
        <label for="requestInput">
            <textarea id="requestInput" class="jsInsert" type="text"></textarea>
        </label>
    </div>
</div>
<br>
<br>
<button id="btn1" class="mySubmitBtn" style="width: auto;"></button>
</body>
<%--<script src="<%=request.getContextPath() %>/static/js/ajax_util.js" type="module"></script>--%>
<script type="module">
    import ajax from "./static/js/ajax_util.js";

    let jsInsertEle = document.getElementById("jsInsert");

    let postForm = () => {
        let json_data = document.getElementById("requestInput").value;
        ajax({
            url: document.getElementById("requestURL").value,
            type: document.getElementById("requestMethod").value,
            data: json_data,
            dataType: 'json',
            timeout: 50000,  //5秒超时
            contentType: "application/json;charset=utf-8",
            ontimeout: (event) => {
                console.log(event);
                jsInsertEle.innerText = "请求超时。";
            }
        })
            .then((recvData, contentType) => {
                console.log(recvData);
                //服务器返回响应，根据响应结果，分析是否请求成功
                let testJsonStr = "{\"flag\":false,\"StudentID\":\"1\",\"CourseID\":\"1\",\"score\":\"80\"}";
                let recvDataObj = JSON.parse(recvData);
                console.log(recvDataObj.success);
                jsInsertEle.innerText = JSON.stringify(recvDataObj);
            }, (e) => {
                console.log(e);
                jsInsertEle.innerText = "请求异常。";
            });
    }

    window.onload = function () {
        let buttonElement = document.getElementById("btn1")
        buttonElement.innerText = "发送请求";
        buttonElement.onclick = postForm;
    }
</script>
</html>
