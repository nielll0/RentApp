<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.Property" %>

<%
  Property p = (Property) request.getAttribute("property");
  if (p == null) {
    response.sendError(500, "Property attribute tidak ada");
    return;
  }
%>

<!DOCTYPE html>
<html>
<head>
  <title>Edit Properti</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <style>
    :root{
      --bg1:#052b31; --bg2:#0a424a; --bg3:#031e22;
      --line: rgba(255,255,255,.10);
      --text:#eaf6f7; --muted: rgba(234,246,247,.72);
      --white:#ffffff; --darkText:#0b2a2f;
      --radius: 18px;
    }
    *{box-sizing:border-box;font-family:Arial,sans-serif}
    body{
      margin:0; min-height:100vh; color:var(--text);
      background: radial-gradient(1200px 700px at 18% 10%, rgba(15,90,97,.55), transparent),
                  linear-gradient(135deg,var(--bg3),var(--bg1),var(--bg2));
    }
    .wrap{width:min(900px,96vw); margin:20px auto;}
    .card{
      padding:16px; border-radius:26px;
      background:rgba(255,255,255,.06);
      border:1px solid var(--line);
    }
    .row{display:grid; grid-template-columns:1fr 1fr; gap:12px;}
    @media(max-width:800px){ .row{grid-template-columns:1fr;} }
    label{font-weight:800; font-size:13px; color:var(--muted)}
    input, textarea, select{
      width:100%; padding:12px 14px; margin-top:6px;
      border-radius:14px; border:1px solid var(--line);
      background:rgba(255,255,255,.06); color:var(--text);
      outline:none;
    }
    textarea{min-height:110px; resize:vertical}
    .btn{
      padding:10px 14px; border-radius:14px;
      background:rgba(255,255,255,.08);
      border:1px solid var(--line);
      font-weight:800; cursor:pointer;
      color:var(--text);
    }
    .btn.white{background:var(--white); color:var(--darkText); border:none;}
    .actions{display:flex; gap:10px; flex-wrap:wrap; margin-top:12px;}
    .thumb{
      width:100%; max-width:260px; height:160px;
      object-fit:cover; border-radius:14px; border:1px solid var(--line);
      background: rgba(255,255,255,.04);
    }
    .muted{color:var(--muted); font-size:13px;}
  </style>
</head>
<body>
<div class="wrap">

  <div class="card">
    <h2 style="margin:0 0 10px;">Edit Properti</h2>
    <p class="muted" style="margin:0 0 14px;">Ubah data properti lalu simpan.</p>

    <form action="<%=request.getContextPath()%>/owner/property/update" method="post" enctype="multipart/form-data">
      <input type="hidden" name="id" value="<%= p.getId() %>" />

      <div class="row">
        <div>
          <label>Judul</label>
          <input type="text" name="judul" required value="<%= p.getJudul()==null?"":p.getJudul() %>"/>
        </div>
        <div>
          <label>Tipe</label>
          <input type="text" name="tipe" required value="<%= p.getTipe()==null?"":p.getTipe() %>"/>
        </div>
      </div>

      <div style="margin-top:12px;">
        <label>Alamat</label>
        <textarea name="alamat" required><%= p.getAlamat()==null?"":p.getAlamat() %></textarea>
      </div>

      <div class="row" style="margin-top:12px;">
        <div>
          <label>Harga per bulan</label>
          <input type="number" name="harga_per_bulan" required value="<%= p.getHargaPerBulan() %>"/>
        </div>
        <div>
          <label>Status</label>
          <select name="action">
            <option value="DRAFT" <%= "DRAFT".equalsIgnoreCase(p.getStatus()) ? "selected" : "" %>>DRAFT</option>
            <option value="PUBLISHED" <%= "PUBLISHED".equalsIgnoreCase(p.getStatus()) ? "selected" : "" %>>PUBLISHED</option>
          </select>
        </div>
      </div>

      <div style="margin-top:12px;">
        <label>Deskripsi</label>
        <textarea name="deskripsi"><%= p.getDeskripsi()==null?"":p.getDeskripsi() %></textarea>
      </div>

      <div class="row" style="margin-top:12px;">
        <div>
          <label>Ganti Foto (opsional)</label>
          <input type="file" name="photo" accept="image/*"/>
          <p class="muted" style="margin:8px 0 0;">Kalau tidak upload, foto lama tetap dipakai.</p>
        </div>
        <div>
          <label>Foto Saat Ini</label><br/>
          <%
            String pp = (p.getPhotoPath()==null) ? "" : p.getPhotoPath().trim();
            String imgUrl = "";
            if (!pp.isEmpty()) {
              imgUrl = pp.startsWith("/") ? (request.getContextPath() + pp) : (request.getContextPath() + "/" + pp);
          %>
            <img class="thumb" src="<%= imgUrl %>" alt="foto"
                 onerror="this.style.display='none';"/>
          <%
            } else {
          %>
            <div class="muted" style="margin-top:10px;">Tidak ada foto</div>
          <%
            }
          %>
        </div>
      </div>

      <div class="actions">
        <button class="btn white" type="submit">Simpan Perubahan</button>
        <a class="btn" href="<%=request.getContextPath()%>/owner/dashboard">Batal</a>
      </div>
    </form>
  </div>

</div>
</body>
</html>