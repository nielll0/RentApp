<%-- 
    Document   : login
    Created on : 11 Dec 2025, 17.48.53
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login RentApp</title>
</head>
<body>
<h2>Login</h2>

<form action="${pageContext.request.contextPath}/login" method="post">
    <label>Email:</label><br>
    <input type="text" name="email" required><br><br>

    <label>Password:</label><br>
    <input type="password" name="password" required><br><br>

    <button type="submit">Login</button>

    <p>Belum punya akun?
        <a href="${pageContext.request.contextPath}/register">Daftar di sini</a>
    </p>
</form>

<p style="color:red">${error}</p>

</body>
</html>