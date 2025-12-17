package filter;

import model.user;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/tenant/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // kosong saja, tidak perlu throw apa pun
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Anti-cache agar tombol Back setelah logout tidak menampilkan halaman protected secara "palsu"
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession(false);
        user u = (session != null) ? (user) session.getAttribute("currentUser") : null;

        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Cek role tenant (karena ini filter untuk /tenant/*)
        String role = u.getRole();
        if (role == null || !"TENANT".equalsIgnoreCase(role.trim())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Halaman ini khusus penyewa.");
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // kosong saja
    }
}
