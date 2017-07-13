<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/11/2017
  Time: 11:09 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
</head>
<body>
    <div class="row">
        <div class="col-xs-4 col-xs-offset-4">
            <h1>Registration</h1>
            <hr>
            <form method="post" action="/register">
                <div class="form-group">
                    <label class="control-label">Username</label>
                    <input type="text" name="username" class="form-control">
                </div>
                <div class="form-group">
                    <label class="control-label">Email</label>
                    <input type="email" name="email" class="form-control">
                </div>
                <div class="form-group">
                    <label class="control-label">Birth date</label>
                    <input type="date" name="birthDate" class="form-control">
                </div>
                <div class="form-group">
                    <label class="control-label">Password</label>
                    <input type="password" name="pwd" class="form-control">
                </div>
                <div class="form-group">
                    <label class="control-label">Repeat password</label>
                    <input type="password" name="pwd2" class="form-control">
                </div>
                <div class="form-group">
                    <label class="control-label">Secret code</label>
                    <input type="text" name="secretCode" class="form-control">
                </div>
                <button class="btn btn-success btn-block" type="submit">Submit</button>
            </form>
        </div>
    </div>
</body>
</html>
