<%-- 
    Document   : register
    Created on : 11 Dec 2025, 18.15.22
    Author     : LENOVO
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Registrasi RentApp</title>
</head>
<body>
<h2>Form Registrasi</h2>

<form action="${pageContext.request.contextPath}/register" method="post">    <label>Nama:</label><br>
    <input type="text" name="nama" required><br><br>

    <label>Email:</label><br>
    <input type="email" name="email" required><br><br>

    <label>Password:</label><br>
    <input type="password" name="password" required><br><br>

    <label>No. Kontak:</label><br>
    <input type="text" name="no_kontak"><br><br>

    <label>Role:</label><br>
    <select name="role" required>
        <option value="TENANT">Penyewa</option>
        <option value="OWNER">Pemilik</option>
    </select><br><br>

    <button type="submit">Daftar</button>
</form>

<p style="color:green;">
    ${success}
</p>
<p style="color:red;">
    ${error}
</p>

<a href="${pageContext.request.contextPath}/login">Sudah punya akun?</a>

</body>
</html>