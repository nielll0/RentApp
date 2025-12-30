package controller;

import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        user u = (session != null) ? (user) session.getAttribute("currentUser") : null;

        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = u.getRole();
        if (role == null || !"ADMIN".equalsIgnoreCase(role.trim())) {
            response.sendError(403, "Hanya ADMIN yang boleh akses halaman ini.");
            return;
        }

        // Sesuai struktur project kamu:
        // Web Pages/views/dashboard/admin.jsp
        request.getRequestDispatcher("/views/dashboard/admin.jsp").forward(request, response);
    }
}