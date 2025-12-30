package controller;

import dao.UserDAO;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/owners/action")
public class AdminOwnerActionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        user u = (session != null) ? (user) session.getAttribute("currentUser") : null;

        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = u.getRole();
        if (role == null || !"ADMIN".equalsIgnoreCase(role.trim())) {
            response.sendError(403, "Hanya ADMIN yang boleh melakukan aksi ini.");
            return;
        }

        String ownerIdRaw = request.getParameter("ownerId");
        String action = request.getParameter("action");

        if (ownerIdRaw == null || ownerIdRaw.isBlank() || action == null || action.isBlank()) {
            response.sendError(400, "Parameter tidak lengkap.");
            return;
        }

        int ownerId;
        try { ownerId = Integer.parseInt(ownerIdRaw.trim()); }
        catch (Exception e) { response.sendError(400, "ownerId tidak valid"); return; }

        try {
            UserDAO dao = new UserDAO();

            if ("suspend".equalsIgnoreCase(action)) {
                dao.updateUserStatus(ownerId, "SUSPENDED");
            } else if ("activate".equalsIgnoreCase(action)) {
                dao.updateUserStatus(ownerId, "ACTIVE");
            } else if ("delete".equalsIgnoreCase(action)) {
                dao.updateUserStatus(ownerId, "DELETED"); // soft delete
            } else {
                response.sendError(400, "Action tidak dikenal.");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/admin/owners");
        } catch (Exception e) {
            throw new ServletException("Gagal update owner: " + e.getMessage(), e);
        }
    }
}