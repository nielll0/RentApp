<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.Property" %>

<!DOCTYPE html>
<html>
<head>
  <title>RentApp - Landing</title>

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
    .wrap{width:min(1200px,96vw); margin:20px auto;}

    .topbar{
      display:flex;
      justify-content:space-between;
      align-items:center;
      padding:14px 16px;
      border-radius:26px;
      background:rgba(255,255,255,.06);
      border:1px solid var(--line);
    }
    .brand{font-weight:900;font-size:18px}

    .actions{display:flex; gap:10px; flex-wrap:wrap;}
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

    .hero{
      margin-top:14px;
      padding:18px;
      border-radius:26px;
      background:rgba(255,255,255,.06);
      border:1px solid var(--line);
    }
    .hero h1{margin:0 0 6px 0; font-size:30px; font-weight:900;}
    .hero p{margin:0; color:var(--muted); line-height:1.6;}

    .heroCta{margin-top:12px; display:flex; gap:10px; flex-wrap:wrap;}
    .linkSmall{color:var(--muted); font-size:13px; margin-top:10px;}

    .section{
      margin-top:14px;
      padding:14px;
      border-radius:26px;
      background:rgba(255,255,255,.06);
      border:1px solid var(--line);
    }

    .search{
      display:flex;
      gap:10px;
      margin-top:10px;
    }
    .search input{
      flex:1;
      padding:12px 14px;
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

    .empty{
      margin-top:10px;
      padding:12px;
      border-radius:14px;
      border:1px dashed rgba(255,255,255,.18);
      color:var(--muted);
    }

    .flow{
      display:grid;
      grid-template-columns:repeat(3,1fr);
      gap:12px;
      margin-top:10px;
    }
    .flowCard{
      padding:12px;
      border-radius:var(--radius);
      background:rgba(255,255,255,.04);
      border:1px solid rgba(255,255,255,.10);
    }
    .flowCard h3{margin:0 0 6px 0;}
    .flowCard p{margin:0; color:var(--muted); line-height:1.6;}

    @media(max-width: 900px){
      .gridProps{grid-template-columns:1fr;}
      .flow{grid-template-columns:1fr;}
    }
  </style>
</head>

<body>
<%
  List<Property> props = (List<Property>) request.getAttribute("properties");
  String q = (String) request.getAttribute("q");
  if (q == null) q = "";
%>

<div class="wrap">

  <!-- TOPBAR: hanya 1 pintu auth -->
  <div class="topbar">
    <div class="brand">RentApp</div>
    <div class="actions">
      <a class="btn" href="<%=request.getContextPath()%>/login">Login</a>
      <a class="btn white" href="<%=request.getContextPath()%>/register">Register</a>
    </div>
  </div>

  <!-- HERO: tidak ada login/register lagi -->
  <div class="hero">
    <h1>Temukan Properti yang Dipublish Pemilik</h1>
    <p>
      Landing page ini fokus jadi “etalase” properti PUBLISHED.
      Pemilik publish properti → otomatis tampil di sini dan bisa dibrowse penyewa.
      Setelah login, sistem akan mengarahkan ke dashboard sesuai role.
    </p>

    <div class="heroCta">
      <a class="btn white" href="#browse">Lihat Properti</a>
      <a class="btn" href="#alur">Cara Kerja</a>
    </div>

    <div class="linkSmall">
      Sudah punya akun? <a href="<%=request.getContextPath()%>/login"><b>Login</b></a>
      · Belum punya? <a href="<%=request.getContextPath()%>/register"><b>Register</b></a>
    </div>
  </div>

  <!-- BROWSE -->
  <div class="section" id="browse">
    <div style="font-weight:900;">Cari Properti</div>
    <form class="search" method="get" action="<%=request.getContextPath()%>/home#browse">
      <input type="text" name="q" value="<%=q%>" placeholder="Cari judul/alamat/tipe..." />
      <button class="btn white" type="submit">Cari</button>
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
            String photo = (p.getPhotoPath()!=null) ? p.getPhotoPath() : "";
        %>
          <div class="propCard">
            <% if (!photo.isEmpty()) { %>
              <img src="<%=request.getContextPath()%>/<%=photo%>" alt="foto properti">
            <% } %>
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

  <!-- FLOW -->
  <div class="section" id="alur">
    <div style="font-weight:900;">Cara Kerja</div>

    <div class="flow">
      <div class="flowCard">
        <h3>1) Pemilik input</h3>
        <p>Pemilik menambahkan data properti (judul, alamat, tipe, harga, foto) lalu publish.</p>
      </div>
      <div class="flowCard">
        <h3>2) Tampil di Landing</h3>
        <p>Properti PUBLISHED otomatis muncul di landing page dan bisa dicari oleh penyewa.</p>
      </div>
      <div class="flowCard">
        <h3>3) Penyewa sewa</h3>
        <p>Penyewa login → pilih properti → ajukan sewa → riwayat sewa tercatat.</p>
      </div>
    </div>
  </div>

</div>
</body>
</html>