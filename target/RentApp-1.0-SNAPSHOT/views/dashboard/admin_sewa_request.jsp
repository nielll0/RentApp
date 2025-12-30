<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.SewaRiwayat" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Admin - Pengajuan Sewa</title>
  <style>
    :root{
      --bg1:#052b31;
      --bg2:#0a424a;
      --card:#0b3a3f;
      --text:#e6f6f7;
      --muted:#b7d7da;
      --accent:#34c6d3;
      --danger:#ff6b6b;
      --shadow: 0 10px 30px rgba(0,0,0,.25);
      --radius: 16px;
    }
    *{ box-sizing:border-box; font-family: Arial, sans-serif; }
    body{
      margin:0;
      color:var(--text);
      background: radial-gradient(1200px 600px at 10% 10%, var(--bg2), var(--bg1));
      min-height:100vh;
    }
    .wrap{ max-width: 980px; margin: 0 auto; padding: 28px 18px; }
    .card{
      background: rgba(11,58,63,.55);
      border:1px solid rgba(255,255,255,.08);
      border-radius: var(--radius);
      padding: 16px;
      box-shadow: var(--shadow);
    }
    h2{ margin:0 0 8px; }
    .top{
      display:flex; justify-content:space-between; align-items:center; gap:12px;
      margin-bottom: 12px;
    }
    a.btn{
      display:inline-block;
      padding: 10px 12px;
      border-radius: 12px;
      text-decoration:none;
      color: var(--text);
      border: 1px solid rgba(255,255,255,.12);
      background: rgba(52,198,211,.10);
    }
    a.btn:hover{ background: rgba(52,198,211,.18); border-color: rgba(52,198,211,.35); }
    table{
      width:100%;
      border-collapse: collapse;
      margin-top: 10px;
    }
    th, td{
      padding: 10px 8px;
      border-bottom: 1px solid rgba(255,255,255,.08);
      text-align:left;
      font-size: 13px;
    }
    th{ color: var(--muted); font-weight:700; }
    .pill{
      padding: 4px 10px;
      border-radius: 999px;
      background: rgba(52,198,211,.12);
      border: 1px solid rgba(52,198,211,.25);
      display:inline-block;
      font-size: 12px;
    }
    button{
      padding: 8px 10px;
      border-radius: 10px;
      border: 1px solid rgba(255,255,255,.15);
      cursor:pointer;
      color: var(--text);
      background: rgba(255,255,255,.06);
    }
    button:hover{ border-color: rgba(52,198,211,.35); background: rgba(52,198,211,.12); }
    button.reject{
      border-color: rgba(255,107,107,.35);
      background: rgba(255,107,107,.10);
    }
    button.reject:hover{
      background: rgba(255,107,107,.16);
    }
    .empty{ color: var(--muted); margin-top: 10px; }
  </style>
</head>
<body>

<div class="wrap">
  <div class="card">
    <div class="top">
      <div>
        <h2>Pengajuan Sewa</h2>
        <div style="color:var(--muted); font-size:13px;">Daftar pengajuan dengan status <b>REQUESTED</b></div>
      </div>
      <a class="btn" href="<%= request.getContextPath() %>/admin/dashboard">Kembali</a>
    </div>

    <%
      List<SewaRiwayat> reqs = (List<SewaRiwayat>) request.getAttribute("requests");
      if (reqs == null || reqs.isEmpty()) {
    %>
        <div class="empty">Tidak ada pengajuan sewa.</div>
    <%
      } else {
    %>

    <table>
      <tr>
        <th>ID</th>
        <th>Properti</th>
        <th>Mulai</th>
        <th>Selesai</th>
        <th>Status</th>
        <th>Total</th>
        <th>Aksi</th>
      </tr>

      <% for (SewaRiwayat r : reqs) { %>
      <tr>
        <td><%= r.getSewaId() %></td>
        <td>
          <div style="font-weight:700;"><%= r.getJudul() %></div>
          <div style="color:var(--muted); font-size:12px;"><%= r.getAlamat() %></div>
        </td>
        <td><%= r.getTanggalMulai() %></td>
        <td><%= r.getTanggalSelesai() %></td>
        <td><span class="pill"><%= r.getStatus() %></span></td>
        <td><%= r.getTotalBiaya() %></td>
        <td>
          <form method="post" action="<%= request.getContextPath() %>/admin/sewa/decide" style="display:inline;">
            <input type="hidden" name="sewaId" value="<%= r.getSewaId() %>"/>
            <button type="submit" name="action" value="approve">Approve</button>
          </form>

          <form method="post" action="<%= request.getContextPath() %>/admin/sewa/decide" style="display:inline;">
            <input type="hidden" name="sewaId" value="<%= r.getSewaId() %>"/>
            <button class="reject" type="submit" name="action" value="reject">Reject</button>
          </form>
        </td>
      </tr>
      <% } %>
    </table>

    <%
      }
    %>

  </div>
</div>

</body>
</html>
