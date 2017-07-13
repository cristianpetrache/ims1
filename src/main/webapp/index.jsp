<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/10/2017
  Time: 1:52 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
</head>
<body>
<div class="row">
    <div class="col-xs-4 col-xs-offset-4">
        <h1>Welcome</h1>
        <hr>
        <form method="get" action="">
            <button type="submit" class="btn btn-success btn-block">Login</button>
        </form>
        <form method="get" action="/register">
            <button type="submit" class="btn btn-primary btn-block">Register</button>
        </form>
    </div>
</div>
</body>
</html>
