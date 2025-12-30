package controller;

import dao.SewaDAO;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/owner/rentals/decide")
public class OwnerRentalDecisionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        user u = (session != null) ? (user) session.getAttribute("currentUser") : null;

        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (u.getRole() == null || !"OWNER".equalsIgnoreCase(u.getRole().trim())) {
            response.sendError(403, "Hanya OWNER.");
            return;
        }

        String sewaIdRaw = request.getParameter("sewaId");
        String action = request.getParameter("action"); // approve / reject

        int sewaId;
        try { sewaId = Integer.parseInt(sewaIdRaw); }
        catch (Exception e) { response.sendError(400, "sewaId tidak valid"); return; }

        try {
            SewaDAO dao = new SewaDAO();

            if ("approve".equalsIgnoreCase(action)) {
                dao.ownerApprove(sewaId, u.getId());
            } else if ("reject".equalsIgnoreCase(action)) {
                dao.ownerReject(sewaId, u.getId());
            } else {
                response.sendError(400, "action tidak valid");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/owner/rentals");

        } catch (Exception e) {
            throw new ServletException("Gagal memproses keputusan owner: " + e.getMessage(), e);
        }
    }
}