<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Tambah Properti - RentApp</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <style>
    :root{
      --bg1:#052b31;
      --bg2:#0a424a;
      --bg3:#031e22;
      --panel: rgba(255,255,255,.10); /* lebih terang */
      --line: rgba(255,255,255,.14);  /* lebih terang */
      --text:#eaf6f7;
      --muted: rgba(234,246,247,.72);
      --white:#ffffff;
      --darkText:#0b2a2f;
      --accent:#7ce7f1;
      --accent2:#20c997;
      --danger:#ff6b6b;
      --radius: 18px;
    }

    *{box-sizing:border-box}
    body{
      margin:0;
      font-family: system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif;
      color:var(--text);
      min-height:100vh;
      background:
        radial-gradient(1200px 700px at 18% 10%, rgba(15,90,97,.55), transparent),
        linear-gradient(135deg,var(--bg3),var(--bg1),var(--bg2));
    }

    a{color:inherit;text-decoration:none}

    .wrap{
      width:min(1100px, 94vw);
      margin:20px auto;
    }

    .topbar{
      display:flex;
      justify-content:space-between;
      align-items:center;
      padding:14px 16px;
      border-radius:26px;
      background:var(--panel);
      border:1px solid var(--line);
    }
    .brand{display:flex;align-items:center;gap:10px;font-weight:900;font-size:18px}
    .dot{
      width:10px;height:10px;border-radius:999px;background:var(--accent);
      box-shadow:0 0 18px rgba(124,231,241,.45);
    }
    .actions{display:flex;gap:10px;flex-wrap:wrap}
    .btn{
      padding:10px 14px;
      border-radius:14px;
      background:rgba(255,255,255,.10);
      border:1px solid var(--line);
      font-weight:800;
      display:inline-flex;
      align-items:center;
      gap:8px;
      cursor:pointer;
    }
    .btn:hover{border-color: rgba(124,231,241,.5)}
    .btn.white{background:var(--white); color:var(--darkText); border:none;}
    .btn.primary{border-color: rgba(124,231,241,.45); background: rgba(124,231,241,.12)}
    .btn.danger{border-color: rgba(255,107,107,.35); background: rgba(255,107,107,.10)}
    .btn.success{border-color: rgba(32,201,151,.45); background: rgba(32,201,151,.18)}

    .card{
      margin-top:14px;
      padding:18px;
      border-radius:26px;
      background:var(--panel);
      border:1px solid var(--line);
    }

    .title{
      display:flex;
      justify-content:space-between;
      gap:12px;
      flex-wrap:wrap;
      align-items:flex-end;
      margin-bottom:12px;
    }
    h1{
      margin:0;
      font-size:26px;
      font-weight:900;
      letter-spacing:.2px;
    }
    .subtitle{
      margin:6px 0 0;
      color:var(--muted);
      line-height:1.6;
      max-width:720px;
      font-size:14px;
    }

    .formGrid{
      display:grid;
      grid-template-columns: 1fr 1fr;
      gap:12px;
      margin-top:14px;
    }
    @media (max-width: 900px){
      .formGrid{grid-template-columns: 1fr;}
    }

    .field{
      background:rgba(255,255,255,.05); /* lebih terang */
      border:1px solid var(--line);
      border-radius:16px;
      padding:12px;
    }
    .field label{
      display:block;
      font-size:13px;
      font-weight:800;
      color:rgba(234,246,247,.88);
      margin-bottom:8px;
      letter-spacing:.2px;
    }
    .req{color: rgba(255,107,107,.9); font-weight:900}

    input[type="text"],
    input[type="number"],
    textarea{
      width:100%;
      padding:12px 12px;
      border-radius:14px;
      border:1px solid rgba(255,255,255,.18);
      background:rgba(255,255,255,.10); /* lebih terang */
      color:var(--text);
      outline:none;
      font-size:14px;
    }
    textarea{min-height:120px; resize:vertical;}
    input::placeholder, textarea::placeholder{color: rgba(234,246,247,.55)}

    .hint{
      margin-top:8px;
      font-size:12px;
      color:var(--muted);
      line-height:1.5;
    }

    .fileRow{
      display:flex;
      gap:10px;
      align-items:center;
      flex-wrap:wrap;
    }
    input[type="file"]{
      width:100%;
      padding:10px 10px;
      border-radius:14px;
      border:1px dashed rgba(255,255,255,.28);
      background:rgba(255,255,255,.06);
      color:var(--muted);
    }
    .fileName{
      font-size:12px;
      color:var(--muted);
    }

    .footerBar{
      display:flex;
      justify-content:space-between;
      align-items:center;
      gap:12px;
      flex-wrap:wrap;
      margin-top:14px;
      padding-top:14px;
      border-top:1px solid var(--line);
    }
    .btnGroup{display:flex; gap:10px; flex-wrap:wrap;}
  </style>
</head>

<body>
  <div class="wrap">

    <div class="topbar">
      <div class="brand"><span class="dot"></span> RentApp</div>
      <div class="actions">
        <a class="btn" href="<%=request.getContextPath()%>/owner/dashboard">Dashboard</a>
        <a class="btn danger" href="<%=request.getContextPath()%>/logout">Logout</a>
      </div>
    </div>

    <div class="card">
      <div class="title">
        <div>
          <h1>Tambah Properti</h1>
          <p class="subtitle">
            Isi data properti dengan lengkap. Klik <b>Publish</b> agar tampil di landing page.
          </p>
        </div>
        <a class="btn" href="<%=request.getContextPath()%>/owner/dashboard">← Kembali</a>
      </div>

      <form action="<%=request.getContextPath()%>/owner/property/save"
            method="post" enctype="multipart/form-data">

        <div class="formGrid">

          <div class="field">
            <label>Judul <span class="req">*</span></label>
            <input type="text" name="judul" required placeholder="Contoh: Kos Ungaran">
            <div class="hint">Nama singkat yang mudah dicari.</div>
          </div>

          <div class="field">
            <label>Tipe <span class="req">*</span></label>
            <input type="text" name="tipe" required placeholder="Contoh: KOS / KONTRAKAN / APARTEMEN">
            <div class="hint">Kategorikan properti kamu.</div>
          </div>

          <div class="field" style="grid-column: 1 / -1;">
            <label>Alamat <span class="req">*</span></label>
            <textarea name="alamat" rows="3" required placeholder="Contoh: Jakarta Selatan, DKI Jakarta"></textarea>
            <div class="hint">Masukkan alamat lengkap atau ringkas (kota/daerah).</div>
          </div>

          <div class="field">
            <label>Harga per bulan (Rp) <span class="req">*</span></label>
            <input type="number" name="harga_per_bulan" required min="0" step="1" placeholder="Contoh: 1500000">
            <div class="hint">Masukkan angka tanpa titik/koma.</div>
          </div>

          <div class="field">
            <label>Foto (jpg/png/webp)</label>
            <div class="fileRow">
              <input id="photoInput" type="file" name="photo" accept="image/*">
              <div id="fileName" class="fileName">Belum ada file dipilih</div>
            </div>
            <div class="hint">Opsional, tapi disarankan agar tampil menarik di landing.</div>
          </div>

          <div class="field" style="grid-column: 1 / -1;">
            <label>Deskripsi</label>
            <textarea name="deskripsi" rows="4" placeholder="Contoh: dekat kampus, parkir luas, wifi, air bersih..."></textarea>
            <div class="hint">Tulis fasilitas/ketentuan/keunggulan properti.</div>
          </div>

        </div>

        <div class="footerBar">
          <div class="hint">
            <span class="req">*</span> Wajib diisi.
          </div>

          <div class="btnGroup">
            <button class="btn success" type="submit" name="action" value="PUBLISHED">Publish</button>
          </div>
        </div>

      </form>
    </div>

  </div>

  <script>
    (function(){
      var input = document.getElementById("photoInput");
      var out = document.getElementById("fileName");
      if (!input || !out) return;

      input.addEventListener("change", function(){
        if (input.files && input.files.length > 0) {
          out.textContent = input.files[0].name;
        } else {
          out.textContent = "Belum ada file dipilih";
        }
      });
    })();
  </script>
</body>
</html>
