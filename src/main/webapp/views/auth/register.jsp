<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Registrasi RentApp</title>
  <style>
    :root{
      --bg1:#031b1f; --bg2:#0b3a3f;
      --pill: rgba(255,255,255,.14);
      --text:#eaf6f7; --muted: rgba(234,246,247,.7);
      --btn:#ffffff; --btnText:#0c2a2e;
    }
    *{box-sizing:border-box;font-family: Arial, sans-serif;}
    body{
      margin:0; min-height:100vh;
      display:flex; align-items:center; justify-content:center;
      background: radial-gradient(1200px 600px at 20% 10%, #0f5a61 0%, transparent 55%),
                  radial-gradient(900px 500px at 85% 30%, #0a4147 0%, transparent 60%),
                  linear-gradient(135deg, var(--bg1), var(--bg2));
      padding: 24px; color: var(--text);
    }
    .wrap{width: min(460px, 100%); text-align:center;}
    h1{margin:0 0 18px 0; letter-spacing:2px; font-size:28px; font-weight:800;}
    .hint{margin:0 0 18px 0; color: var(--muted); font-size:14px;}
    .field{
      position: relative;
      margin: 12px 0;
      border-radius: 999px;
      background: var(--pill);
      padding: 14px 18px;
      backdrop-filter: blur(6px);
      text-align:left;
    }
    .field input, .field select{
      width: 100%;
      border: none; outline: none;
      background: transparent;
      color: var(--text);
      font-size: 15px;
      appearance: none;
    }
    .field input::placeholder{color: var(--muted);}
    .btn{
      margin-top: 14px; width: 100%;
      border: none; border-radius: 999px;
      padding: 14px 16px;
      font-weight: 800; letter-spacing: 1px;
      background: var(--btn); color: var(--btnText);
      cursor: pointer; box-shadow: 0 10px 25px rgba(0,0,0,.25);
    }
    .footer{margin-top: 14px; color: var(--muted); font-size: 14px;}
    .footer a{color: var(--text); text-decoration: underline;}
  </style>
</head>
<body>
  <div class="wrap">
    <h1>REGISTRASI</h1>
    <p class="hint">Isi data sesuai role (penyewa/pemilik).</p>

    <form action="${pageContext.request.contextPath}/register" method="post">
      <div class="field">
        <input type="text" name="nama" placeholder="Nama Lengkap" required>
      </div>

      <div class="field">
        <input type="email" name="email" placeholder="Email" required>
      </div>

      <div class="field">
        <input type="password" name="password" placeholder="Password" required>
      </div>

      <div class="field">
        <input type="text" name="no_kontak" placeholder="No. Kontak" required>
      </div>

      <div class="field">
        <select name="role" required>
          <option value="TENANT">Penyewa</option>
          <option value="OWNER">Pemilik</option>
        </select>
      </div>

      <button class="btn" type="submit">DAFTAR</button>
    </form>

    <div class="footer">
      Sudah punya akun? <a href="${pageContext.request.contextPath}/login">Login</a>
    </div>
  </div>
</body>
</html>
