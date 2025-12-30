<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.user"%>
<%
    // Ambil currentUser dari session (sesuai pola project kamu)
    user u = (session != null) ? (user) session.getAttribute("currentUser") : null;

    // Kalau belum login, arahkan ke login (biar aman)
    if (u == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String nama = (u.getNama() != null) ? u.getNama() : "Admin";
    String email = (u.getEmail() != null) ? u.getEmail() : "-";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>RentApp - Admin Dashboard</title>
    <style>
        :root{
            --bg1:#052b31;
            --bg2:#0a424a;
            --panel:#06363d;
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
        .wrap{
            max-width: 980px;
            margin: 0 auto;
            padding: 28px 18px;
        }
        .topbar{
            display:flex;
            align-items:center;
            justify-content:space-between;
            gap:16px;
            background: rgba(6,54,61,.65);
            border:1px solid rgba(255,255,255,.08);
            padding: 14px 16px;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
        }
        .brand{
            display:flex;
            align-items:center;
            gap:12px;
        }
        .logo{
            width:42px; height:42px;
            border-radius: 12px;
            background: linear-gradient(135deg, rgba(52,198,211,.25), rgba(52,198,211,.05));
            border: 1px solid rgba(52,198,211,.35);
            display:flex; align-items:center; justify-content:center;
            font-weight:bold;
            color:var(--accent);
        }
        .brand h1{
            font-size: 18px;
            margin:0;
            line-height:1.2;
        }
        .brand p{
            margin:2px 0 0;
            color:var(--muted);
            font-size: 12px;
        }
        .userbox{
            text-align:right;
        }
        .userbox .name{
            font-weight:700;
            font-size: 14px;
            margin:0;
        }
        .userbox .email{
            margin:2px 0 0;
            color:var(--muted);
            font-size: 12px;
        }

        .grid{
            margin-top: 18px;
            display:grid;
            grid-template-columns: 1.2fr .8fr;
            gap: 16px;
        }
        @media (max-width: 860px){
            .grid{ grid-template-columns: 1fr; }
            .userbox{ text-align:left; }
            .topbar{ flex-direction:column; align-items:flex-start; }
        }

        .card{
            background: rgba(11,58,63,.55);
            border:1px solid rgba(255,255,255,.08);
            border-radius: var(--radius);
            padding: 16px;
            box-shadow: var(--shadow);
        }
        .card h2{
            margin:0 0 8px;
            font-size: 16px;
        }
        .card p{
            margin:0 0 14px;
            color:var(--muted);
            font-size: 13px;
            line-height:1.5;
        }
        .actions{
            display:flex;
            flex-wrap:wrap;
            gap:10px;
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
        a.btn:hover{
            background: rgba(52,198,211,.18);
            border-color: rgba(52,198,211,.35);
        }
        a.btn.secondary{
            background: rgba(255,255,255,.06);
        }
        a.btn.danger{
            background: rgba(255,107,107,.10);
            border-color: rgba(255,107,107,.25);
        }
        a.btn.danger:hover{
            background: rgba(255,107,107,.16);
            border-color: rgba(255,107,107,.35);
        }
        .hint{
            margin-top:10px;
            font-size:12px;
            color:var(--muted);
        }
        .footer{
            margin-top: 18px;
            color: rgba(230,246,247,.7);
            font-size: 12px;
            text-align:center;
        }
        code{
            background: rgba(0,0,0,.25);
            padding: 2px 6px;
            border-radius: 8px;
        }
    </style>
</head>
<body>
<div class="wrap">

    <div class="topbar">
        <div class="brand">
            <div class="logo">RA</div>
            <div>
                <h1>RentApp — Admin Dashboard</h1>
                <p>Kelola pengajuan sewa, validasi transaksi, dan manajemen akun OWNER</p>
            </div>
        </div>
        <div class="userbox">
            <p class="name"><%= nama %> (ADMIN)</p>
            <p class="email"><%= email %></p>
        </div>
    </div>

    <div class="grid">
        <div class="card">
            <h2>Pengajuan Sewa</h2>
            <p>
                Lihat daftar pengajuan sewa dengan status <b>REQUESTED</b>, lalu lakukan
                <b>APPROVE</b> atau <b>REJECT</b>.
            </p>

            <div class="actions">
                <a class="btn" href="<%= request.getContextPath() %>/admin/sewa/requests">
                    Lihat Pengajuan (REQUESTED)
                </a>
            </div>

            <div class="hint">
                Catatan: Hanya yang <b>APPROVED</b> yang boleh lanjut ke pembayaran.
            </div>
        </div>

        <div class="card">
            <h2>Manajemen Owner</h2>
            <p>
                Fitur ini untuk suspend / soft delete akun OWNER.
                Saat OWNER disuspend, properti tidak bisa disewa namun data lama tetap aman.
            </p>

            <div class="actions">
                <!-- Nanti kita buat endpoint-nya -->
                <a class="btn secondary" href="<%= request.getContextPath() %>/admin/owners">
                    Kelola Owner (Suspend/Hapus)
                </a>
                <a class="btn danger" href="<%= request.getContextPath() %>/logout">
                    Logout
                </a>
            </div>

            <div class="hint">
                Jika link <code>/admin/owners</code> belum ada, nanti kita buat setelah approve/reject beres.
            </div>
        </div>
    </div>

    <div class="footer">
        RentApp (Servlet + JSP + JDBC) — sesuai konsep PBO: MVC sederhana + role-based access.
    </div>

</div>
</body>
</html>