package controller;

import dao.SewaDAO;
import model.SewaRiwayat;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/sewa/requests")
public class AdminSewaRequestsServlet extends HttpServlet {

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

        try {
            List<SewaRiwayat> list = new SewaDAO().findAllRequested();
            request.setAttribute("requests", list);

            // Path utama sesuai struktur kamu (views/dashboard/...)
            String jsp = "/views/dashboard/admin_sewa_request.jsp";

            // Forward
            request.getRequestDispatcher(jsp).forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Gagal load pengajuan sewa: " + e.getMessage(), e);
        }
    }
}