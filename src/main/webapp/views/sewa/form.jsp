<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Form Sewa</title>
  <style>
    :root{
      --bg1:#052b31; --bg2:#0a424a; --bg3:#031e22;
      --text:#eaf6f7; --muted:rgba(234,246,247,.72);
      --line:rgba(255,255,255,.12);
      --pill:rgba(255,255,255,.14);
      --white:#fff; --dark:#0b2a2f;
      --radius:18px;
    }
    *{box-sizing:border-box;font-family:Arial,sans-serif;}
    body{
      margin:0; min-height:100vh; color:var(--text);
      display:flex; align-items:center; justify-content:center;
      padding: 24px;
      background:
        radial-gradient(1200px 700px at 18% 10%, rgba(15,90,97,.55) 0%, transparent 55%),
        radial-gradient(900px 520px at 85% 30%, rgba(10,65,71,.6) 0%, transparent 60%),
        linear-gradient(135deg, var(--bg3), var(--bg1), var(--bg2));
    }
    .card{
      width:min(520px, 100%);
      border:1px solid var(--line);
      background:rgba(255,255,255,.06);
      border-radius: 26px;
      padding: 18px;
      box-shadow: 0 18px 50px rgba(0,0,0,.35);
      backdrop-filter: blur(10px);
    }
    h2{margin:0 0 6px 0;font-size:22px;font-weight:900;}
    .sub{color:var(--muted);margin:0 0 14px 0;font-size:13px;}
    .error{
      margin: 10px 0;
      padding: 10px 12px;
      border-radius: 14px;
      background: rgba(255,0,0,.10);
      border: 1px solid rgba(255,0,0,.18);
      color: #ffd4d4;
    }
    label{display:block;margin:12px 0 6px 0;font-weight:800;}
    input{
      width:100%;
      padding: 12px 14px;
      border-radius: 999px;
      border:1px solid rgba(255,255,255,.14);
      background: var(--pill);
      outline:none;
      color: var(--text);
    }
    .row{display:flex;gap:10px;margin-top:14px;}
    .btn{
      flex:1;
      padding: 12px 14px;
      border-radius: 999px;
      border: 1px solid rgba(255,255,255,.16);
      background: rgba(255,255,255,.08);
      color: var(--text);
      font-weight:900;
      cursor:pointer;
      text-align:center;
      display:inline-block;
    }
    .btn.white{
      background: var(--white);
      color: var(--dark);
      border:none;
      box-shadow: 0 10px 25px rgba(0,0,0,.25);
    }
  </style>
</head>
<body>

<%
  // propertyId bisa datang dari link /sewaForm?propertyId=... atau dari forward (attribute)
  String pid = request.getParameter("propertyId");
  if (pid == null) pid = (String) request.getAttribute("propertyId");
  if (pid == null) pid = "";
  pid = pid.trim();

  // min tanggal mulai = hari ini
  String today = java.time.LocalDate.now().toString();

  // kalau propertyId kosong, tampilkan error biar gak insert sewa "null"
  String localError = null;
  if (pid.isEmpty()) {
      localError = "Property ID tidak ditemukan. Silakan kembali dan klik tombol Sewa dari daftar properti.";
  }
%>

<div class="card">
  <h2>Form Sewa</h2>
  <p class="sub">Isi tanggal mulai & selesai untuk mengajukan sewa.</p>

  <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
  <% } %>

  <% if (localError != null) { %>
    <div class="error"><%= localError %></div>
  <% } %>

  <form action="<%= request.getContextPath() %>/tenant/sewa/submit" method="post">
    <input type="hidden" name="propertyId" value="<%= pid %>">

    <label>Tanggal Mulai</label>
    <input type="date" name="tanggalMulai" min="<%= today %>" required <%= pid.isEmpty() ? "disabled" : "" %> >

    <label>Tanggal Selesai</label>
    <input type="date" name="tanggalSelesai" min="<%= today %>" required <%= pid.isEmpty() ? "disabled" : "" %> >

    <div class="row">
      <a class="btn" href="<%= request.getContextPath() %>/tenant/browse">Kembali</a>
      <button class="btn white" type="submit" <%= pid.isEmpty() ? "disabled" : "" %>>
        Lanjut ke Pembayaran
      </button>
    </div>
  </form>
</div>

</body>
</html>