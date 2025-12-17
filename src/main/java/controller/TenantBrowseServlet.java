package controller;

import dao.PropertyDAO;
import model.Property;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/tenant/browse")
public class TenantBrowseServlet extends HttpServlet {

    private PropertyDAO propertyDAO;
    private static final int LIMIT = 20;

    @Override
    public void init() {
        propertyDAO = new PropertyDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Anti-cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Safety (AuthFilter sudah cover /tenant/*, tapi ini double check)
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
            List<Property> properties = propertyDAO.searchPublishedLimit(q, LIMIT);

            request.setAttribute("q", q);
            request.setAttribute("properties", properties);
            request.setAttribute("currentUser", u);

            request.getRequestDispatcher("/views/dashboard/tenant_browse.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Gagal load browse properti (SQL): " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ServletException("Gagal load browse properti: " + e.getMessage(), e);
        }
    }
}