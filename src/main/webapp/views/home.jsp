<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="model.Property" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>

<!DOCTYPE html>
<html>
<head>
  <title>RentApp - Landing</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />

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
      cursor:pointer;
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

    .thumbWrap{
      width:100%;
      height:140px;
      border-radius:14px;
      border:1px solid rgba(255,255,255,.10);
      margin-bottom:10px;
      overflow:hidden;
      background: rgba(255,255,255,.04);
      display:flex;
      align-items:center;
      justify-content:center;
      color: rgba(234,246,247,.55);
      font-size:12px;
      font-weight:800;
      letter-spacing:.2px;
    }

    .thumbWrap img{
      width:100%;
      height:100%;
      object-fit:cover;
      object-position:center;
      display:block;
    }

    .propTitle{
      font-weight:900;
      margin:0 0 6px 0;
      font-size:16px;
      line-height:1.2;

      display:-webkit-box;
      -webkit-line-clamp:2;
      -webkit-box-orient:vertical;
      overflow:hidden;
    }

    .propMeta{
      color:var(--muted);
      font-size:13px;
      margin:0 0 4px 0;

      display:-webkit-box;
      -webkit-line-clamp:2;
      -webkit-box-orient:vertical;
      overflow:hidden;
    }

    .typePill{
      margin-top:8px;
      display:inline-block;
      padding:6px 10px;
      border-radius:999px;
      border:1px solid rgba(255,255,255,.12);
      background: rgba(255,255,255,.06);
      font-size:12px;
      font-weight:800;
      color: rgba(234,246,247,.85);
    }

    .empty{
      margin-top:10px;
      padding:12px;
      border-radius:14px;
      border:1px dashed rgba(255,255,255,.18);
      color:var(--muted);
    }

    @media(max-width: 900px){
      .gridProps{grid-template-columns:1fr;}
    }
  </style>
</head>

<body>
<%
  List<Property> props = (List<Property>) request.getAttribute("properties");
  String q = (String) request.getAttribute("q");
  if (q == null) q = "";

  NumberFormat rupiah = NumberFormat.getInstance(new Locale("id","ID"));
  rupiah.setMaximumFractionDigits(0);
%>

<div class="wrap">

  <div class="topbar">
    <div class="brand">RentApp</div>
    <div class="actions">
      <a class="btn" href="<%=request.getContextPath()%>/login">Login</a>
      <a class="btn white" href="<%=request.getContextPath()%>/register">Register</a>
    </div>
  </div>

  <div class="hero">
    <h1>Temukan Properti yang Dipublish Pemilik</h1>
    <p>
      Landing page ini fokus jadi “etalase” properti PUBLISHED.
      Pemilik publish properti → otomatis tampil di sini dan bisa dibrowse penyewa.
      Setelah login, sistem akan mengarahkan ke dashboard sesuai role.
    </p>

    <div class="heroCta">
      <a class="btn white" href="#browse">Lihat Properti</a>
    </div>

    <div class="linkSmall">
      Sudah punya akun? <a href="<%=request.getContextPath()%>/login"><b>Login</b></a>
      · Belum punya? <a href="<%=request.getContextPath()%>/register"><b>Register</b></a>
    </div>
  </div>

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
            String photoPath = (p.getPhotoPath() != null) ? p.getPhotoPath().trim() : "";
            String imgUrl = "";

            if (!photoPath.isEmpty()) {
              imgUrl = photoPath.startsWith("/")
                      ? (request.getContextPath() + photoPath)
                      : (request.getContextPath() + "/" + photoPath);
            }

            String judul = (p.getJudul()!=null && !p.getJudul().trim().isEmpty()) ? p.getJudul() : "(Tanpa Judul)";
            String alamat = (p.getAlamat()!=null && !p.getAlamat().trim().isEmpty()) ? p.getAlamat() : "-";
            String tipe = (p.getTipe()!=null && !p.getTipe().trim().isEmpty()) ? p.getTipe() : "-";
        %>
          <div class="propCard">

            <div class="thumbWrap">
              <% if (!imgUrl.isEmpty()) { %>
                <img src="<%= imgUrl %>"
                     alt="foto properti"
                     onerror="this.style.display='none'; this.parentElement.textContent='Tanpa foto';">
              <% } else { %>
                Tanpa foto
              <% } %>
            </div>

            <div class="propTitle"><%= judul %></div>
            <div class="propMeta"><%= alamat %></div>
            <div class="propMeta">Rp <%= rupiah.format(p.getHargaPerBulan()) %> / bulan</div>

            <span class="typePill">🏷️ <%= tipe %></span>

          </div>
        <%
          }
        %>
      </div>
    <%
      }
    %>
  </div>

</div>
</body>
</html>