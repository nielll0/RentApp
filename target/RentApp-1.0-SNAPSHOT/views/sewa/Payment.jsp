<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="model.SewaRiwayat" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Pembayaran Sewa - RentApp</title>
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
      --shadow: 0 20px 60px rgba(0,0,0,.35);
    }

    *{box-sizing:border-box;font-family:Arial,sans-serif}
    body{
      margin:0;
      min-height:100vh;
      color:var(--text);
      background:
        radial-gradient(1200px 700px at 18% 10%, rgba(15,90,97,.55), transparent),
        linear-gradient(135deg,var(--bg3),var(--bg1),var(--bg2));
      display:flex;
      align-items:center;
      justify-content:center;
      padding:18px;
    }

    .card{
      width:min(760px, 100%);
      border-radius:26px;
      background:rgba(255,255,255,.06);
      border:1px solid var(--line);
      box-shadow: var(--shadow);
      overflow:hidden;
    }

    .head{
      padding:18px 18px 12px;
      border-bottom:1px solid var(--line);
      display:flex;
      justify-content:space-between;
      align-items:center;
      gap:10px;
    }
    .title{
      font-size:22px;
      font-weight:900;
      margin:0;
    }
    .sub{
      margin-top:6px;
      color:var(--muted);
      font-size:13px;
      line-height:1.6;
    }

    .body{
      padding:18px;
      display:grid;
      grid-template-columns: 1.2fr .8fr;
      gap:14px;
    }

    .panel{
      padding:14px;
      border-radius:18px;
      border:1px solid var(--line);
      background:rgba(255,255,255,.04);
    }

    .row{
      display:flex;
      justify-content:space-between;
      gap:12px;
      padding:10px 0;
      border-bottom:1px solid rgba(255,255,255,.06);
      font-size:14px;
    }
    .row:last-child{ border-bottom:none; }
    .k{ color:var(--muted); }
    .v{ font-weight:900; }

    .pill{
      display:inline-block;
      padding:6px 10px;
      border-radius:999px;
      border:1px solid rgba(255,255,255,.12);
      background: rgba(255,255,255,.06);
      font-size:12px;
      font-weight:900;
    }

    .actions{
      display:flex;
      gap:10px;
      flex-wrap:wrap;
      margin-top:12px;
    }

    .btn{
      padding:10px 14px;
      border-radius:14px;
      background:rgba(255,255,255,.08);
      border:1px solid var(--line);
      font-weight:900;
      cursor:pointer;
      display:inline-block;
      color:var(--text);
      text-decoration:none;
    }

    .btn.white{
      background:var(--white);
      color:var(--darkText);
      border:none;
    }

    .note{
      margin-top:10px;
      color:var(--muted);
      font-size:12px;
      line-height:1.6;
    }

    @media(max-width: 880px){
      .body{ grid-template-columns: 1fr; }
    }
  </style>
</head>
<body>

<%
  SewaRiwayat s = (SewaRiwayat) request.getAttribute("sewa");

  NumberFormat rupiah = NumberFormat.getInstance(new Locale("id","ID"));
  rupiah.setMaximumFractionDigits(0);

  String judul = (s!=null && s.getJudul()!=null) ? s.getJudul() : "-";
  String alamat = (s!=null && s.getAlamat()!=null) ? s.getAlamat() : "-";
  String mulai = (s!=null && s.getTanggalMulai()!=null) ? String.valueOf(s.getTanggalMulai()) : "-";
  String selesai = (s!=null && s.getTanggalSelesai()!=null) ? String.valueOf(s.getTanggalSelesai()) : "-";
  String status = (s!=null && s.getStatus()!=null) ? s.getStatus().trim().toUpperCase() : "-";
  long total = (s!=null) ? s.getTotalBiaya() : 0L;
%>

<div class="card">
  <div class="head">
    <div>
      <div class="title">Pembayaran Sewa</div>
      <div class="sub">
        Konfirmasi pembayaran hanya tersedia jika status <b>APPROVED</b>.
      </div>
    </div>
    <a class="btn" href="<%= request.getContextPath() %>/tenant/dashboard?tab=riwayat">Kembali</a>
  </div>

  <div class="body">
    <div class="panel">
      <div class="row">
        <div class="k">Properti</div>
        <div class="v"><%= judul %></div>
      </div>
      <div class="row">
        <div class="k">Alamat</div>
        <div class="v" style="font-weight:700;"><%= alamat %></div>
      </div>
      <div class="row">
        <div class="k">Tanggal</div>
        <div class="v"><%= mulai %> s/d <%= selesai %></div>
      </div>
      <div class="row">
        <div class="k">Status</div>
        <div class="v"><span class="pill"><%= status %></span></div>
      </div>
    </div>

    <div class="panel">
      <div style="font-weight:900; font-size:13px; color:var(--muted);">TOTAL</div>
      <div style="font-weight:900; font-size:28px; margin-top:8px;">
        Rp <%= rupiah.format(total) %>
      </div>

      <div class="actions">
        <form method="post" action="<%= request.getContextPath() %>/tenant/payment/pay" style="margin:0;">
          <input type="hidden" name="sewaId" value="<%= (s!=null)? s.getSewaId() : 0 %>"/>
          <button class="btn white" type="submit"
                  onclick="return confirm('Konfirmasi bayar? Status akan berubah menjadi PAID.');">
            Konfirmasi Bayar
          </button>
        </form>

        <a class="btn" href="<%= request.getContextPath() %>/tenant/dashboard?tab=riwayat">Nanti dulu</a>
      </div>

      <div class="note">
        * Ini pembayaran dummy. Setelah bayar, status akan menjadi <b>PAID</b>.
      </div>
    </div>
  </div>
</div>

</body>
</html>