<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Login RentApp</title>
  <style>
    :root{
      --bg1:#031b1f;
      --bg2:#0b3a3f;
      --card: rgba(255,255,255,.06);
      --pill: rgba(255,255,255,.14);
      --text:#eaf6f7;
      --muted: rgba(234,246,247,.7);
      --btn:#ffffff;
      --btnText:#0c2a2e;
    }
    *{box-sizing:border-box;font-family: Arial, sans-serif;}
    body{
      margin:0; min-height:100vh;
      display:flex; align-items:center; justify-content:center;
      background: radial-gradient(1200px 600px at 20% 10%, #0f5a61 0%, transparent 55%),
                  radial-gradient(900px 500px at 85% 30%, #0a4147 0%, transparent 60%),
                  linear-gradient(135deg, var(--bg1), var(--bg2));
      padding: 24px;
      color: var(--text);
    }
    .wrap{width: min(420px, 100%); text-align:center;}
    h1{
      margin: 0 0 26px 0;
      letter-spacing: 2px;
      font-size: 30px;
      font-weight: 800;
    }
    .field{
      position: relative;
      margin: 16px 0;
      border-radius: 999px;
      background: var(--pill);
      padding: 14px 54px;
      backdrop-filter: blur(6px);
    }
    .field input{
      width: 100%;
      border: none;
      outline: none;
      background: transparent;
      color: var(--text);
      font-size: 15px;
    }
    .field input::placeholder{color: var(--muted);}
    .icon-left, .icon-right{
      position:absolute; top:50%; transform:translateY(-50%);
      width: 42px; height: 42px;
      border-radius: 999px;
      display:flex; align-items:center; justify-content:center;
      background: rgba(255,255,255,.16);
    }
    .icon-left{left: 8px;}
    .icon-right{right: 8px;}
    .btn{
      margin-top: 22px;
      width: 100%;
      border: none;
      border-radius: 999px;
      padding: 14px 16px;
      font-weight: 800;
      letter-spacing: 1px;
      background: var(--btn);
      color: var(--btnText);
      cursor: pointer;
      box-shadow: 0 10px 25px rgba(0,0,0,.25);
    }
    .btn:hover{transform: translateY(-1px);}
    .footer{
      margin-top: 16px;
      color: var(--muted);
      font-size: 14px;
    }
    .footer a{color: var(--text); text-decoration: underline;}
  </style>
</head>
<body>
  <div class="wrap">
    <h1>USER LOGIN</h1>

    <form action="${pageContext.request.contextPath}/login" method="post">
      <div class="field">
        <span class="icon-left" aria-hidden="true">
          <!-- user icon -->
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <path d="M12 12a4.5 4.5 0 1 0-4.5-4.5A4.5 4.5 0 0 0 12 12Z" stroke="white" stroke-width="2"/>
            <path d="M4 20c1.8-3.3 5-5 8-5s6.2 1.7 8 5" stroke="white" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </span>
        <input type="email" name="email" placeholder="Email" required>
      </div>

      <div class="field">
        <span class="icon-left" aria-hidden="true">
          <!-- key icon -->
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <path d="M7 14a5 5 0 1 1 9.6 2H21v3h-3v3h-3v-3H9.4A5 5 0 0 1 7 14Z"
                  stroke="white" stroke-width="2" stroke-linejoin="round"/>
          </svg>
        </span>

        <input id="pw" type="password" name="password" placeholder="Password" required>

        <span class="icon-right" aria-hidden="true" onclick="togglePw()" style="cursor:pointer" title="Show/Hide">
          <!-- lock icon -->
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none">
            <path d="M7 11V8a5 5 0 0 1 10 0v3" stroke="white" stroke-width="2" stroke-linecap="round"/>
            <path d="M6 11h12v10H6V11Z" stroke="white" stroke-width="2" stroke-linejoin="round"/>
          </svg>
        </span>
      </div>

      <button class="btn" type="submit">LOGIN</button>
    </form>

    <div class="footer">
      Belum punya akun? <a href="${pageContext.request.contextPath}/register">Daftar di sini</a>
    </div>
  </div>

  <script>
    function togglePw(){
      const el = document.getElementById('pw');
      el.type = (el.type === 'password') ? 'text' : 'password';
    }
  </script>
</body>
</html>
