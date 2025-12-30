package controller;

import dao.PropertyDAO;
import dao.SewaDAO;
import model.Property;
import model.SewaRiwayat;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/tenant/dashboard")
public class TenantDashboardServlet extends HttpServlet {

    private PropertyDAO propertyDAO;
    private SewaDAO sewaDAO;

    private static final int RECOMMENDED_LIMIT = 20;

    @Override
    public void init() {
        propertyDAO = new PropertyDAO();
        sewaDAO = new SewaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession(false);
        user u = (session != null) ? (user) session.getAttribute("currentUser") : null;

        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = u.getRole();
        if (role == null || !"TENANT".equalsIgnoreCase(role.trim())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Halaman ini khusus penyewa.");
            return;
        }

        String q = request.getParameter("q");
        if (q != null) {
            q = q.trim();
            if (q.isEmpty()) q = null;
        }

        try {
            List<Property> recommended = propertyDAO.searchPublishedLimit(q, RECOMMENDED_LIMIT);
            List<SewaRiwayat> riwayat = sewaDAO.findByTenantId(u.getId());

            // ✅ definisi "aktif" yang masuk akal untuk user:
            // masih proses / jalan: APPROVED (nunggu bayar) + PAID + ACTIVE
            int sewaAktif = sewaDAO.countByTenantInStatuses(u.getId(), "APPROVED", "PAID", "ACTIVE");
            int sewaSelesai = sewaDAO.countByTenantAndStatus(u.getId(), "SELESAI");
            long totalBiaya = sewaDAO.sumTotalBiayaByTenant(u.getId());

            request.setAttribute("q", q);
            request.setAttribute("recommendedProperties", recommended);
            request.setAttribute("riwayat", riwayat);
            request.setAttribute("sewaAktif", sewaAktif);
            request.setAttribute("sewaSelesai", sewaSelesai);
            request.setAttribute("totalBiaya", totalBiaya);
            request.setAttribute("currentUser", u);

            request.getRequestDispatcher("/views/dashboard/penyewa.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Gagal load dashboard tenant (SQL): " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ServletException("Gagal load dashboard tenant: " + e.getMessage(), e);
        }
    }
}