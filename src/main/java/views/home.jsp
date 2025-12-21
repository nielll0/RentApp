<%-- 
    Document   : home
    Created on : 11 Dec 2025, 22.42.22
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="model.user"%>

<%
  HttpSession s = request.getSession(false);
  user u = (s != null) ? (user) s.getAttribute("currentUser") : null;
  String role = (s != null) ? (String) s.getAttribute("role") : null;

  ResultSet rs = (ResultSet) request.getAttribute("rs");
  String q = (String) request.getAttribute("q");
  if (q == null) q = "";
%>

<!DOCTYPE html>
<html>
<head>
  <title>RentApp - Home</title>
  <style>
    body{font-family:Arial, sans-serif;margin:0;background:#f7f7f8;color:#111;}
    .container{max-width:1100px;margin:0 auto;padding:18px;}
    .nav{display:flex;justify-content:space-between;align-items:center;padding:10px 0;}
    .brand{font-weight:800;font-size:20px;}
    .nav a{margin-left:10px;text-decoration:none;color:#2563eb;}
    .search{display:flex;gap:10px;background:#fff;border:1px solid #e5e7eb;border-radius:14px;padding:10px;margin:14px 0;}
    .search input{border:0;outline:none;flex:1;font-size:14px;}
    .search button{border:0;background:#2563eb;color:#fff;padding:10px 14px;border-radius:12px;cursor:pointer;}
    .grid{display:grid;grid-template-columns:repeat(4,minmax(0,1fr));gap:14px;}
    .card{background:#fff;border:1px solid #e5e7eb;border-radius:14px;overflow:hidden;}
    .thumb{height:120px;background:#eef2ff;}
    .content{padding:12px;}
    .title{font-weight:700;margin:0 0 6px 0;}
    .meta{color:#666;font-size:13px;margin-bottom:10px;min-height:34px;}
    .row{display:flex;justify-content:space-between;align-items:center;gap:10px;}
    .badge{font-size:12px;padding:4px 8px;border-radius:999px;background:#f3f4f6;color:#111;}
    .price{font-weight:800;}
    .btn{display:inline-block;margin-top:10px;padding:9px 10px;border-radius:12px;background:#2563eb;color:#fff;text-decoration:none;text-align:center;width:100%;}
    .empty{padding:18px;background:#fff;border:1px dashed #d1d5db;border-radius:14px;color:#555;}
    @media(max-width:980px){.grid{grid-template-columns:repeat(2,1fr);}}
    @media(max-width:520px){.grid{grid-template-columns:1fr;}}
  </style>
</head>
<body>
  <div class="container">
    <div class="nav">
      <div class="brand">RentApp</div>
      <div>
        <% if (u == null) { %>
          <a href="<%= request.getContextPath() %>/views/auth/login.jsp">Login</a>
          <a href="<%= request.getContextPath() %>/views/auth/register.jsp">Register</a>
        <% } else { %>
          <span>Halo, <%= u.getNama() %> (<%= role %>)</span>
          <%-- kalau kamu punya dashboard tenant --%>
          <% if ("TENANT".equals(role)) { %>
            <a href="<%= request.getContextPath() %>/tenant/dashboard">Dashboard</a>
          <% } %>
          <a href="<%= request.getContextPath() %>/logout">Logout</a>
        <% } %>
      </div>
    </div>

    <h2>Daftar Properti</h2>

    <form class="search" method="get" action="<%= request.getContextPath() %>/">
      <input type="text" name="q" placeholder="Cari judul/alamat/tipe..." value="<%= q %>">
      <button type="submit">Cari</button>
    </form>

    <% boolean ada = false; %>
    <div class="grid">
      <%
        if (rs != null) {
          while (rs.next()) {
            ada = true;
      %>
        <div class="card">
          <div class="thumb"></div>
          <div class="content">
            <div class="row">
              <div class="badge"><%= rs.getString("tipe") %></div>
              <div class="price">Rp <%= rs.getBigDecimal("harga_per_bulan") %>/bln</div>
            </div>
            <p class="title"><%= rs.getString("judul") %></p>
            <div class="meta"><%= rs.getString("alamat") %></div>

            <a class="btn"
               href="<%= request.getContextPath() %>/sewaForm?propertyId=<%= rs.getInt("id") %>">
              Sewa
            </a>
          </div>
        </div>
      <%
          }
        }
      %>
    </div>

    <% if (!ada) { %>
      <div class="empty">Belum ada properti yang dipublish (atau hasil pencarian kosong).</div>
    <% } %>

  </div>
</body>
</html>
