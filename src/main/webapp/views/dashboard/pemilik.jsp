<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.DecimalFormatSymbols" %>
<%@ page import="model.user" %>
<%@ page import="model.Property" %>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    HttpSession sess = request.getSession(false);
    user currentUser = (sess == null) ? null : (user) sess.getAttribute("currentUser");
    String role = (sess == null) ? null : (String) sess.getAttribute("role");

    if (currentUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    if (!"OWNER".equals(role)) {
        response.sendError(403);
        return;
    }

    List<Property> properties = (List<Property>) request.getAttribute("properties");
    if (properties == null) properties = new ArrayList<>();

    int totalProperti = properties.size();
    int published = 0;
    for (Property p : properties) {
        String st = p.getStatus();
        if (st != null && "PUBLISHED".equalsIgnoreCase(st.trim())) published++;
    }

    // formatter harga: 1500000 -> 1.500.000
    DecimalFormatSymbols sym = new DecimalFormatSymbols(new java.util.Locale("id","ID"));
    sym.setGroupingSeparator('.');
    sym.setDecimalSeparator(',');
    DecimalFormat rupiah = new DecimalFormat("#,##0", sym);
%>

<!DOCTYPE html>
<html>
<head>
    <title>RentApp - Dashboard Pemilik</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <style>
        :root{
            --bg1:#052b31;
            --bg2:#0a424a;
            --bg3:#031e22;
            --text:#e8f6f7;
            --muted:#b8d7d9;
            --line:rgba(232,246,247,.14);
            --accent:#7ce7f1;
            --danger:#ff6b6b;
        }
        *{box-sizing:border-box}
        body{
            margin:0;
            font-family: system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif;
            color:var(--text);
            background: radial-gradient(1200px 800px at 20% 10%, var(--bg2), var(--bg1) 45%, var(--bg3) 100%);
            min-height:100vh;
        }
        a{color:inherit; text-decoration:none}
        .wrap{max-width:1100px; margin:0 auto; padding:24px;}
        .nav{
            display:flex; gap:14px; align-items:center; justify-content:space-between;
            padding:14px 16px; border:1px solid var(--line);
            background: rgba(7,48,56,.55); backdrop-filter: blur(8px);
            border-radius:16px;
        }
        .brand{display:flex; align-items:center; gap:10px; font-weight:800; letter-spacing:.4px}
        .dot{width:10px; height:10px; border-radius:999px; background:var(--accent)}
        .navlinks{display:flex; gap:10px; flex-wrap:wrap}
        .btn{
            display:inline-flex; align-items:center; justify-content:center; gap:8px;
            padding:10px 12px; border-radius:12px;
            border:1px solid var(--line);
            background: rgba(255,255,255,.04);
            color:var(--text);
            cursor:pointer;
        }
        .btn:hover{border-color: rgba(124,231,241,.55)}
        .btn.primary{
            border-color: rgba(124,231,241,.45);
            background: rgba(124,231,241,.10);
        }
        .btn.danger{
            border-color: rgba(255,107,107,.35);
            background: rgba(255,107,107,.10);
        }

        .header{
            display:flex; align-items:flex-end; justify-content:space-between; gap:16px;
            margin-top:18px;
        }
        .title h1{margin:0; font-size:28px;}
        .title p{margin:6px 0 0; color:var(--muted)}

        .grid{
            margin-top:16px;
            display:grid;
            grid-template-columns: repeat(3, 1fr);
            gap:12px;
        }
        @media (max-width: 900px){
            .grid{grid-template-columns: 1fr;}
            .header{flex-direction:column; align-items:flex-start}
        }
        .card{
            border:1px solid var(--line);
            border-radius:16px;
            background: rgba(11,47,53,.55);
            backdrop-filter: blur(8px);
            padding:14px 14px;
        }
        .card h3{margin:0 0 6px; font-size:14px; color:var(--muted); font-weight:700}
        .big{font-size:26px; font-weight:900; margin:0}
        .hint{margin:8px 0 0; font-size:12px; color:var(--muted)}

        .section{
            margin-top:14px;
            border:1px solid var(--line);
            border-radius:16px;
            background: rgba(7,48,56,.45);
            backdrop-filter: blur(8px);
            overflow:hidden;
        }
        .sectionHead{
            display:flex; align-items:center; justify-content:space-between; gap:10px;
            padding:14px 14px;
            border-bottom:1px solid var(--line);
        }
        .sectionHead h2{margin:0; font-size:16px}
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
        th{color:var(--muted); font-weight:800; font-size:12px; text-transform:uppercase; letter-spacing:.6px}
        .badge{
            display:inline-flex; align-items:center;
            padding:6px 10px; border-radius:999px;
            border:1px solid var(--line);
            background: rgba(255,255,255,.04);
            font-size:12px; color:var(--muted);
        }
        .badge.ok{border-color: rgba(32,201,151,.35); color: #bff3e1; background: rgba(32,201,151,.12);}
        .badge.warn{border-color: rgba(124,231,241,.35); color: #d6fbff; background: rgba(124,231,241,.10);}

        .empty{padding:18px 14px; color:var(--muted);}
        .thumb{
            width:84px; height:56px; object-fit:cover;
            border-radius:10px; border:1px solid var(--line);
            background: rgba(255,255,255,.04);
        }
        .thumbWrap{
            width:84px; height:56px; border-radius:10px; border:1px solid var(--line);
            background: rgba(255,255,255,.04);
            display:flex; align-items:center; justify-content:center;
            overflow:hidden;
            color: rgba(232,246,247,.55);
            font-size:10px;
            font-weight:800;
            letter-spacing:.3px;
        }
        .mono{font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", monospace;}
    </style>
</head>

<body>
<div class="wrap">

    <div class="nav">
        <div class="brand">
            <span class="dot"></span>
            <span>RentApp</span>
        </div>

        <div class="navlinks">
            <a class="btn primary" href="<%= request.getContextPath() %>/owner/dashboard">Dashboard Pemilik</a>
            <a class="btn danger" href="<%= request.getContextPath() %>/logout">Logout</a>
        </div>
    </div>

    <div class="header">
        <div class="title">
            <h1>Dashboard Pemilik</h1>
            <p>Halo, <b><%= currentUser.getNama() %></b> — kelola properti dan pantau aktivitas sewa.</p>
        </div>

        <div style="display:flex; gap:10px; flex-wrap:wrap;">
            <a class="btn primary" href="<%= request.getContextPath() %>/owner/property/new">+ Tambah Properti</a>
            <a class="btn" href="<%= request.getContextPath() %>/owner/rentals">Lihat Pengajuan Sewa</a>
        </div>
    </div>

    <div class="grid">
        <div class="card">
            <h3>Total Properti</h3>
            <p class="big"><%= totalProperti %></p>
            <p class="hint">Jumlah properti yang tercatat untuk pemilik ini.</p>
        </div>

        <div class="card">
            <h3>Published</h3>
            <p class="big"><%= published %></p>
            <p class="hint">Properti yang tampil di landing / browse penyewa.</p>
        </div>

        <div class="card">
            <h3>Status Akun</h3>
            <p class="big" style="margin-bottom:6px;">Aktif</p>
            <span class="badge ok">Role: OWNER</span>
            <p class="hint">Email: <span class="mono"><%= currentUser.getEmail() %></span></p>
        </div>
    </div>

    <div class="section">
        <div class="sectionHead">
            <h2>Properti Saya</h2>
            <div style="display:flex; gap:10px; flex-wrap:wrap;">
                <a class="btn" href="<%=request.getContextPath()%>/owner/dashboard">Refresh</a>
            </div>
        </div>

        <div class="tableWrap">
            <%
                if (properties.isEmpty()) {
            %>
                <div class="empty">
                    Belum ada properti untuk pemilik ini.
                </div>
            <%
                } else {
            %>
                <table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Foto</th>
                        <th>Judul</th>
                        <th>Alamat</th>
                        <th>Harga / Bulan</th>
                        <th>Status</th>
                        <th>Aksi</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        int no = 1;
                        for (Property p : properties) {
                            String statusP = (p.getStatus() == null) ? "DRAFT" : p.getStatus().trim();
                            boolean isPub = "PUBLISHED".equalsIgnoreCase(statusP);

                            String photoPath = (p.getPhotoPath() != null) ? p.getPhotoPath().trim() : "";
                            String imgUrl = "";
                            if (!photoPath.isEmpty()) {
                                imgUrl = photoPath.startsWith("/")
                                        ? (request.getContextPath() + photoPath)
                                        : (request.getContextPath() + "/" + photoPath);
                            }

                            String hargaTxt = "Rp " + rupiah.format(p.getHargaPerBulan()) + " / bulan";
                    %>
                        <tr>
                            <td><%= no++ %></td>

                            <td>
                                <div class="thumbWrap">
                                    <% if (!imgUrl.isEmpty()) { %>
                                        <img class="thumb" src="<%= imgUrl %>" alt="foto"
                                             onerror="this.style.display='none'; this.parentElement.textContent='NO IMAGE';">
                                    <% } else { %>
                                        NO IMAGE
                                    <% } %>
                                </div>
                            </td>

                            <td><b><%= (p.getJudul() == null || p.getJudul().isBlank()) ? "(Tanpa Judul)" : p.getJudul() %></b></td>
                            <td><%= (p.getAlamat() == null || p.getAlamat().isBlank()) ? "-" : p.getAlamat() %></td>

                            <td><%= hargaTxt %></td>

                            <td>
                                <span class="badge <%= isPub ? "ok" : "warn" %>">
                                    <%= isPub ? "PUBLISHED" : "DRAFT" %>
                                </span>
                            </td>

                            <td style="display:flex; gap:8px; flex-wrap:wrap;">
                                <a class="btn" href="<%= request.getContextPath() %>/owner/property/edit?id=<%= p.getId() %>">Edit</a>
                                <a class="btn danger"
                                   href="<%= request.getContextPath() %>/owner/property/delete?id=<%= p.getId() %>"
                                   onclick="return confirm('Hapus properti ini?')">Hapus</a>
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
</body>
</html>