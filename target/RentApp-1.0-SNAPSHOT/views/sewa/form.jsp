<%-- 
    Document   : form
    Created on : 13 Dec 2025, 01.40.59
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Form Sewa</title>
</head>
<body>

<h2>Form Sewa</h2>

<p style="color:red;">
    ${error}
</p>

<form action="<%= request.getContextPath() %>/sewaSubmit" method="post">

    <input type="hidden" name="propertyId" value="${propertyId}">

    <label>Tanggal Mulai:</label><br>
    <input type="date" name="tanggalMulai" required><br><br>

    <label>Tanggal Selesai:</label><br>
    <input type="date" name="tanggalSelesai" required><br><br>

    <button type="submit">Kirim Pengajuan</button>
</form>

</body>
</html>