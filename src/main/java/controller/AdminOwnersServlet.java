package controller;

import dao.UserDAO;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/owners")
public class AdminOwnersServlet extends HttpServlet {

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
            request.setAttribute("owners", new UserDAO().findOwners());
            request.getRequestDispatcher("/views/dashboard/admin_owners.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Gagal load owners: " + e.getMessage(), e);
        }
    }
}