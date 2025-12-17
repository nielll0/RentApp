<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.user" %>
<%@ page import="model.Property" %>

<!DOCTYPE html>
<html>
<head>
  <title>Cari Properti - RentApp</title>

  <style>
    :root{
      --bg1:#052b31;
      --bg2:#0a424a;
      --bg3:#031e22;
      --panel: rgba(255,255,255,.06);
      --line: rgba(255,255,255,.10);
      --text:#eaf6f7;
      --muted: rgba(234,246,247,.72);
      --white:#ffffff;
      --darkText:#0b2a2f;
      --radius: 18px;
    }

    *{box-sizing:border-box;font-family:Arial,sans-serif}
    body{
      margin:0;
      min-height:100vh;
      color:var(--text);
      background:
        radial-gradient(1200px 700px at 18% 10%, rgba(15,90,97,.55), transparent),
        linear-gradient(135deg,var(--bg3),var(--bg1),var(--bg2));
    }
    a{text-decoration:none;color:inherit}

    .app{
      width:min(1200px,96vw);
      margin:20px auto;
      display:flex;
      border-radius:26px;
      overflow:hidden;
      border:1px solid var(--line);
      background:rgba(255,255,255,.04);
      min-height: calc(100vh - 40px);
    }

    .sidebar{
      width:250px;
      padding:16px;
      border-right:1px solid var(--line);
      background:rgba(255,255,255,.02);
      position:relative;
      z-index:5;
    }
    .brand{font-weight:900;font-size:18px;margin-bottom:12px}

    .nav a{
      display:block;
      padding:12px;
      margin-bottom:6px;
      border-radius:14px;
      color:var(--muted);
      border:1px solid transparent;
      position:relative;
      z-index:5;
    }
    .nav a.active,
    .nav a:hover{
      background:rgba(255,255,255,.10);
      color:var(--text);
      border:1px solid rgba(255,255,255,.12);
    }

    .main{flex:1;padding:18px}

    .topbar{
      display:flex;
      justify-content:space-between;
      align-items:center;
      padding:14px;
      border-radius:var(--radius);
      background:rgba(255,255,255,.06);
      margin-bottom:14px;
      border:1px solid var(--line);
    }

    .hello .big{font-size:26px;font-weight:900}
    .hello .small{font-size:13px;color:var(--muted)}

    .btn{
      padding:10px 14px;
      border-radius:14px;
      background:rgba(255,255,255,.08);
      border:1px solid var(--line);
      font-weight:800;
      display:inline-block;
    }
    .btn.white{
      background:var(--white);
      color:var(--darkText);
      border:none;
    }

    .section{
      margin-top:14px;
      padding:14px;
      border-radius:var(--radius);
      background:rgba(255,255,255,.06);
      border:1px solid var(--line);
    }

    .search{
      display:flex;
      gap:10px;
      margin-top:10px;
      margin-bottom:12px;
    }
    .search input{
      flex:1;
      padding:10px 12px;
      border-radius:14px;
      border:1px solid var(--line);
      background:rgba(255,255,255,.06);
      color:var(--text);
      outline:none;
    }

    .gridProps{
      display:grid;
      grid-template-columns:repeat(3,1fr);
      gap:12px;
      margin-top:12px;
    }

    .propCard{
      padding:12px;
      border-radius:var(--radius);
      background:rgba(255,255,255,.06);
      border:1px solid var(--line);
    }
    .propCard img{
      width:100%;
      height:140px;
      object-fit:cover;
      border-radius:14px;
      border:1px solid rgba(255,255,255,.10);
      margin-bottom:10px;
    }
    .propTitle{font-weight:900;margin-bottom:6px}
    .propMeta{color:var(--muted);font-size:13px;margin-bottom:4px}

    .empty{color:var(--muted);padding:8px 0}

    @media(max-width: 900px){
      .app{flex-direction:column;}
      .sidebar{width:100%; border-right:none; border-bottom:1px solid var(--line);}
      .gridProps{grid-template-columns:1fr;}
    }
  </style>
</head>

<body>
<%
  // Data dari TenantBrowseServlet
  user u = (user) request.getAttribute("currentUser");
  String nama = (u!=null && u.getNama()!=null) ? u.getNama() : "Penyewa";

  String q = (String) request.getAttribute("q");
  List<Property> props = (List<Property>) request.getAttribute("properties");

  // Penanda menu aktif untuk halaman ini
  String current = "browse";
%>

<div class="app">

  <!-- SIDEBAR -->
  <aside class="sidebar">
    <div class="brand">RentApp</div>
    <div class="nav">

      <a class="<%= "dashboard".equals(current) ? "active" : "" %>"
         href="<%=request.getContextPath()%>/tenant/dashboard?tab=dashboard">
        Dashboard
      </a>

      <a class="<%= "browse".equals(current) ? "active" : "" %>"
         href="<%=request.getContextPath()%>/tenant/browse">
        Cari Properti
      </a>

      <a class="<%= "riwayat".equals(current) ? "active" : "" %>"
         href="<%=request.getContextPath()%>/tenant/dashboard?tab=riwayat">
        Riwayat Sewa
      </a>

      <a href="<%=request.getContextPath()%>/logout">
        Logout
      </a>

    </div>
  </aside>

  <!-- MAIN -->
  <main class="main">

    <div class="topbar">
      <div class="hello">
        <div class="small">Welcome back</div>
        <div class="big"><%= nama %></div>
        <div class="small">Cari Properti Tersedia</div>
      </div>
      <div>
        <a class="btn" href="<%=request.getContextPath()%>/tenant/dashboard?tab=dashboard">Dashboard</a>
        <a class="btn white" href="<%=request.getContextPath()%>/logout">Logout</a>
      </div>
    </div>

    <div class="section">
      <h3>Cari Properti</h3>

      <form class="search" method="get" action="<%=request.getContextPath()%>/tenant/browse">
        <input type="text" name="q" value="<%= (q!=null)? q : "" %>" placeholder="Cari judul/alamat/tipe..." />
        <button class="btn" type="submit">Cari</button>
      </form>

      <%
        if (props == null || props.isEmpty()) {
      %>
        <div class="empty">Belum ada properti yang dipublish (atau hasil pencarian kosong).</div>
      <%
        } else {
      %>
        <div class="gridProps">
          <%
            for (Property p : props) {
              String photo = (p.getPhotoPath() != null) ? p.getPhotoPath() : "";
          %>
            <div class="propCard">
              <%
                if (!photo.isEmpty()) {
              %>
                <img src="<%=request.getContextPath()%>/<%=photo%>" alt="foto properti">
              <%
                }
              %>

              <div class="propTitle"><%= p.getJudul() %></div>
              <div class="propMeta"><%= p.getAlamat() %></div>
              <div class="propMeta">Rp <%= p.getHargaPerBulan() %> / bulan</div>
              <div class="propMeta"><%= p.getTipe() %></div>
            </div>
          <%
            }
          %>
        </div>
      <%
        }
      %>

    </div>

  </main>
</div>

</body>
</html>