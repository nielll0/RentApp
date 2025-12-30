package controller;

import dao.PropertyDAO;
import model.Property;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="PemilikDashboardServlet", urlPatterns={"/owner/dashboard"})
public class PemilikDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        user cu = (session == null) ? null : (user) session.getAttribute("currentUser");
        String role = (session == null) ? null : (String) session.getAttribute("role");

        // 1) Guard login
        if (cu == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2) Guard role
        if (!"OWNER".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // 3) Ambil data properti milik owner dari DB
        List<Property> properties = new ArrayList<>();
        try {
            PropertyDAO dao = new PropertyDAO();
            properties = dao.getByPemilik(cu.getId()); // <--- INI KUNCINYA
        } catch (Exception e) {
            // Kalau mau debugging, bisa tampilkan pesan error yang jelas
            throw new ServletException("Gagal load properti milik owner (SQL): " + e.getMessage(), e);
        }

        // 4) Kirim ke JSP
        request.setAttribute("properties", properties);

        // 5) Forward
        request.getRequestDispatcher("/views/dashboard/pemilik.jsp").forward(request, response);
    }
}