<%-- 
    Document   : list
    Created on : 13 Dec 2025, 00.55.36
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="util.DBConnection"%>

<!DOCTYPE html>
<html>
<head>
    <title>Daftar Properti</title>
</head>
<body>

<h2>Daftar Properti</h2>
<p><a href="<%= request.getContextPath() %>/">Home</a></p>

<table border="1" cellpadding="8">
    <tr>
        <th>Judul</th>
        <th>Alamat</th>
        <th>Harga/Bulan</th>
        <th>Tipe</th>
        <th>Aksi</th>
    </tr>

<%
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = DBConnection.getConnection();
        String sql = "SELECT id, judul, alamat, harga_per_bulan, tipe FROM properties WHERE status='PUBLISHED' ORDER BY created_at DESC";
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();

        while (rs.next()) {
%>
    <tr>
        <td><%= rs.getString("judul") %></td>
        <td><%= rs.getString("alamat") %></td>
        <td><%= rs.getBigDecimal("harga_per_bulan") %></td>
        <td><%= rs.getString("tipe") %></td>
        <td>
            <a href="<%= request.getContextPath() %>/sewaForm?propertyId=<%= rs.getInt("id") %>">Sewa</a>
        </td>
    </tr>
<%
        }

    } catch (Exception e) {
%>
    <tr><td colspan="5" style="color:red">Error: <%= e.getMessage() %></td></tr>
<%
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception ex) {}
        try { if (ps != null) ps.close(); } catch (Exception ex) {}
        try { if (con != null) con.close(); } catch (Exception ex) {}
    }
%>

</table>

</body>
</html>