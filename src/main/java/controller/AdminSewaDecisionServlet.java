package controller;

import dao.SewaDAO;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/sewa/decide")
public class AdminSewaDecisionServlet extends HttpServlet {

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

        String sewaIdRaw = request.getParameter("sewaId");
        String action = request.getParameter("action");

        if (sewaIdRaw == null || sewaIdRaw.isBlank() || action == null || action.isBlank()) {
            response.sendError(400, "Parameter tidak lengkap.");
            return;
        }

        int sewaId;
        try { sewaId = Integer.parseInt(sewaIdRaw.trim()); }
        catch (Exception e) { response.sendError(400, "sewaId tidak valid"); return; }

        try {
            SewaDAO dao = new SewaDAO();

            if ("approve".equalsIgnoreCase(action)) {
                dao.adminApprove(sewaId);
            } else if ("reject".equalsIgnoreCase(action)) {
                dao.adminReject(sewaId);
            } else {
                response.sendError(400, "Action tidak dikenal.");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/admin/sewa/requests");
        } catch (Exception e) {
            throw new ServletException("Gagal proses keputusan admin: " + e.getMessage(), e);
        }
    }
}