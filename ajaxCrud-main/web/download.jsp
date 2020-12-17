<%@ page import="java.util.ArrayList" %>
<%@ page import="vo.Download" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %><%--
  Created by IntelliJ IDEA.
  User: 28492
  Date: 2020/9/19
  Time: 19:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
    <title>下载</title>
</head>
<link rel="stylesheet" href="css/download.css">
<script type="text/javascript" src="js/download.js"></script>
<body>


<div class="box">
<c:forEach items="${sessionScope.dList}"  var="download" varStatus="vs" >
    <img src="${download.image}" alt="1" class="flag">
<div class="name">${download.name}</div>
<br>
<span class="size">大小: ${download.size}</span>

    <span>&nbsp;&nbsp;&nbsp;&nbsp;时间：</span>
<span>&nbsp;&nbsp;&nbsp;星级：</span>

    <c:forEach var="brightStar" begin="1" end="${download.star}">
        <img src="file/brightStar.png" alt="亮星星" class="star">
    </c:forEach>

    <c:forEach  var="darkstar" begin="1" end="${5-download.star}" step="1">
        <img src="file/darkStar.png" alt="暗星星" class="star">
    </c:forEach>

    <input type="button" value="立即下载" onclick="downloadDoc('${download.path}')"/>
    <br>
<span class="description">${download.description}</span>
<hr/>
</c:forEach>
</div>

</body>
</html>
