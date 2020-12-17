<%--
  Created by IntelliJ IDEA.
  User: 28492
  Date: 2020/9/18
  Time: 19:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>主页</title>
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
<img src="file/mainHead.png" alt="1">
<span class="userInfo">欢迎您： ${sessionScope.user.chrName}<a href="servlet/Invalidate.do">【安全退出】</a></span>
<ul>
    <li><a href="main.jsp">首页</a></li>
    <li>|</li>
    <li><a href="getDownloadList.do">资源下载</a></li>
    <li>|</li>
    <li><a href="userManage.html">用户管理</a></li>
    <li>|</li>
    <li><a href="resourceManage.jsp">资源管理</a></li>
    <li>|</li>
    <li><a href="profile.jsp">个人中心</a></li>
</ul>
<img src="file/main.png" alt="2" class="main">
</body>
</html>
