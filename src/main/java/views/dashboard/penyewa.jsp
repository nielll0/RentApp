<%-- 
    Document   : penyewa
    Created on : 11 Dec 2025, 17.51.01
    Author     : LENOVO
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="util.DBConnection"%>
<%@page import="model.user"%>

<%
    // 1. CEK SUDAH LOGIN ATAU BELUM
    user currentUser = (user) session.getAttribute("currentUser");
    if (currentUser == null) {
        // kalau belum login, lempar ke /login
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard Penyewa</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .header { margin-bottom: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        .btn { 
            display:inline-block; 
            padding:8px 12px; 
            background:#007bff; 
            color:#fff; 
            text-decoration:none; 
            border-radius:4px;
        }
        .btn:hover { background:#0056b3; }
    </style>
</head>
<body>

<div class="header">
    <h2>Halo, <%= currentUser.getNama() %> 👋</h2>
    <p>Role: PENYEWA</p>

    <!-- nanti halaman ini akan kita buat belakangan -->
    <a class="btn" href="<%= request.getContextPath() %>/views/properties/list.jsp">
        Lihat Daftar Properti
    </a>
</div>

<h3>Riwayat Sewa Anda</h3>

<table>
    <tr>
        <th>ID Sewa</th>
        <th>Judul Properti</th>
        <th>Alamat</th>
        <th>Tanggal Mulai</th>
        <th>Tanggal Selesai</th>
        <th>Status</th>
        <th>Total Biaya</th>
    </tr>

<%
    // 2. AMBIL DATA SEWA DARI DATABASE UNTUK PENYEWA INI
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = DBConnection.getConnection();

        String sql = 
            "SELECT s.id, p.judul, p.alamat, s.tanggal_mulai, s.tanggal_selesai, " +
            "       s.status, s.total_biaya " +
            "FROM sewa s " +
            "JOIN properties p ON s.property_id = p.id " +
            "WHERE s.penyewa_id = ? " +
            "ORDER BY s.created_at DESC";

        ps = con.prepareStatement(sql);
        ps.setInt(1, currentUser.getId());   // pastikan ada getId() di class user
        rs = ps.executeQuery();

        while (rs.next()) {
%>
    <tr>
        <td><%= rs.getInt("id") %></td>
        <td><%= rs.getString("judul") %></td>
        <td><%= rs.getString("alamat") %></td>
        <td><%= rs.getDate("tanggal_mulai") %></td>
        <td><%= rs.getDate("tanggal_selesai") %></td>
        <td><%= rs.getString("status") %></td>
        <td><%= rs.getBigDecimal("total_biaya") %></td>
    </tr>
<%
        }

    } catch (Exception e) {
%>
    <tr>
        <td colspan="7" style="color:red">
            Error mengambil data sewa: <%= e.getMessage() %>
        </td>
    </tr>
<%
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            // abaikan
        }
    }
%>

</table>

</body>
</html>