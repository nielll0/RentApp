<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="model.SewaRiwayat" %>

<!DOCTYPE html>
<html>
<head>
  <title>Pengajuan Sewa - RentApp</title>
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
      background:rgba(255,255,255,.06);
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
      background:rgba(255,255,255,.08);
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
    .btn.success{border-color: rgba(32,201,151,.40); background: rgba(32,201,151,.12)}
    button.btn{outline:none}

    .card{
      margin-top:14px;
      padding:18px;
      border-radius:26px;
      background:rgba(255,255,255,.06);
      border:1px solid var(--line);
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
      font-size:14px;
    }

    .grid{
      margin-top:14px;
      display:grid;
      grid-template-columns: 1fr 1fr 1fr 1fr;
      gap:12px;
    }

    @media (max-width: 900px){
      .grid{grid-template-columns: 1fr;}
    }

    .mini{
      border:1px solid var(--line);
      border-radius:18px;
      background:rgba(0,0,0,.10);
      padding:12px;
    }
    .mini .k{font-size:12px;color:var(--muted);font-weight:800;letter-spacing:.4px;text-transform:uppercase}
    .mini .v{margin-top:8px;font-size:22px;font-weight:900}

    .tableBox{
      margin-top:14px;
      border:1px solid var(--line);
      border-radius:18px;
      overflow:hidden;
      background:rgba(0,0,0,.10);
    }
    .tableHead{
      display:flex;
      align-items:center;
      justify-content:space-between;
      gap:12px;
      padding:12px 14px;
      border-bottom:1px solid var(--line);
    }
    .tableHead .ttl{font-weight:900}
    .tableWrap{overflow:auto;}
    table{
      width:100%;
      border-collapse:collapse;
      min-width:860px;
    }
    th, td{
      padding:12px 14px;
      border-bottom:1px solid var(--line);
      text-align:left;
      font-size:14px;
      vertical-align:top;
    }
    th{
      color:var(--muted);
      font-weight:800;
      font-size:12px;
      text-transform:uppercase;
      letter-spacing:.6px
    }

    .badge{
      display:inline-flex;
      padding:6px 10px;
      border-radius:999px;
      border:1px solid var(--line);
      background: rgba(255,255,255,.04);
      font-size:12px;
      color:var(--muted);
      font-weight:800;
    }
    .badge.ok{border-color: rgba(32,201,151,.40); background: rgba(32,201,151,.12); color: #bff3e1;}
    .badge.warn{border-color: rgba(124,231,241,.35); background: rgba(124,231,241,.10); color: #d6fbff;}
    .badge.danger{border-color: rgba(255,107,107,.35); background: rgba(255,107,107,.10); color: #ffd1d1;}

    .empty{
      padding:16px 14px;
      color:var(--muted);
      line-height:1.7;
    }
    .empty b{color:var(--text)}
    form{display:inline;}
  </style>
</head>

<body>
<%
  List<SewaRiwayat> rentals = (List<SewaRiwayat>) request.getAttribute("rentals");
  Integer countRequested = (Integer) request.getAttribute("countRequested");
  Integer countApproved  = (Integer) request.getAttribute("countApproved");
  Integer countPaid      = (Integer) request.getAttribute("countPaid");
  Integer countRejected  = (Integer) request.getAttribute("countRejected");

  if (countRequested == null) countRequested = 0;
  if (countApproved == null) countApproved = 0;
  if (countPaid == null) countPaid = 0;
  if (countRejected == null) countRejected = 0;

  NumberFormat rupiah = NumberFormat.getInstance(new Locale("id","ID"));
  rupiah.setMaximumFractionDigits(0);
%>


  <div class="wrap">

    <div class="topbar">
      <div class="brand"><span class="dot"></span> RentApp</div>
      <div class="actions">
        <a class="btn" href="<%=request.getContextPath()%>/owner/dashboard">Dashboard</a>
        <a class="btn danger" href="<%=request.getContextPath()%>/logout">Logout</a>
      </div>
    </div>

    <div class="card">
      <div style="display:flex;justify-content:space-between;gap:12px;flex-wrap:wrap;align-items:flex-end;">
        <div>
          <h1>Pengajuan Sewa</h1>
          <p class="subtitle">
            Pemilik meninjau pengajuan sewa untuk properti miliknya.
            Alur status: <b>REQUESTED</b> (menunggu), <b>APPROVED</b> (disetujui), <b>PAID</b> (sudah dibayar), <b>REJECTED</b> (ditolak).
          </p>
        </div>
        <a class="btn" href="<%=request.getContextPath()%>/owner/dashboard">← Kembali</a>
      </div>

      <div class="grid">
          <div class="mini">
              <div class="k">Requested</div>
              <div class="v"><%= countRequested %></div>
            </div>
          <div class="mini">
              <div class="k">Approved</div>
              <div class="v"><%= countApproved %></div>
            </div>
          <div class="mini">
              <div class="k">Paid</div>
              <div class="v"><%= countPaid %></div>
            </div>
          <div class="mini">
              <div class="k">Rejected</div>
              <div class="v"><%= countRejected %></div>
          </div>
      </div>


      <div class="tableBox">
        <div class="tableHead">
          <div class="ttl">Daftar Pengajuan</div>
          <a class="btn" href="<%=request.getContextPath()%>/owner/rentals">Refresh</a>
        </div>

        <div class="tableWrap">
          <%
            if (rentals == null || rentals.isEmpty()) {
          %>
            <div class="empty">
              <b>Belum ada pengajuan sewa.</b><br/>
              Kalau kamu yakin sudah ada sewa di tabel <b>sewa</b>, berarti servlet/DAO owner belum JOIN ke properties.
            </div>
          <%
            } else {
          %>
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Properti</th>
                  <th>Mulai</th>
                  <th>Selesai</th>
                  <th>Total</th>
                  <th>Status</th>
                  <th>Aksi</th>
                </tr>
              </thead>
              <tbody>
              <%
                for (SewaRiwayat s : rentals) {
                  String st = (s.getStatus()==null) ? "" : s.getStatus().trim().toUpperCase();

                  String badgeCls = "badge";
                  if ("REQUESTED".equals(st)) badgeCls = "badge warn";
                  else if ("APPROVED".equals(st) || "PAID".equals(st) || "ACTIVE".equals(st)) badgeCls = "badge ok";
                  else if ("REJECTED".equals(st)) badgeCls = "badge danger";

              %>
                <tr>
                  <td><%= s.getSewaId() %></td>
                  <td>
                    <div style="font-weight:900;"><%= (s.getJudul()!=null)?s.getJudul():"-" %></div>
                    <div style="color:var(--muted); font-size:13px;"><%= (s.getAlamat()!=null)?s.getAlamat():"-" %></div>
                  </td>
                  <td><%= s.getTanggalMulai() %></td>
                  <td><%= s.getTanggalSelesai() %></td>
                  <td>Rp <%= rupiah.format(s.getTotalBiaya()) %></td>
                  <td><span class="<%= badgeCls %>"><%= st %></span></td>
                  <td>
                    <% if ("REQUESTED".equals(st)) { %>
                      <form method="post" action="<%=request.getContextPath()%>/owner/rentals/decide">
                        <input type="hidden" name="sewaId" value="<%= s.getSewaId() %>">
                        <button class="btn success" type="submit" name="action" value="approve">Approve</button>
                      </form>

                      <form method="post" action="<%=request.getContextPath()%>/owner/rentals/decide">
                        <input type="hidden" name="sewaId" value="<%= s.getSewaId() %>">
                        <button class="btn danger" type="submit" name="action" value="reject"
                          onclick="return confirm('Reject pengajuan ini?');">
                          Reject
                        </button>
                      </form>
                    <% } else { %>
                      -
                    <% } %>
                  </td>
                </tr>
              <%
                }
              %>
              </tbody>
            </table>
          <%
            }
          %>
        </div>
      </div>

    </div>

  </div>
</body>
</html>