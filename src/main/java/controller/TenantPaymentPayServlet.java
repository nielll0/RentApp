package controller;

import dao.SewaDAO;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/tenant/payment/pay")
public class TenantPaymentPayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        user u = (session != null) ? (user) session.getAttribute("currentUser") : null;

        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Safety: pastikan tenant
        String role = u.getRole();
        if (role == null || !"TENANT".equalsIgnoreCase(role.trim())) {
            response.sendError(403, "Hanya penyewa yang boleh bayar.");
            return;
        }

        String sewaIdRaw = request.getParameter("sewaId");
        if (sewaIdRaw == null || sewaIdRaw.isBlank()) {
            response.sendError(400, "sewaId wajib ada");
            return;
        }

        int sewaId;
        try { sewaId = Integer.parseInt(sewaIdRaw.trim()); }
        catch (Exception e) { response.sendError(400, "sewaId tidak valid"); return; }

        try {
            // APPROVED -> PAID saja
            new SewaDAO().markPaidByIdForTenant(sewaId, u.getId());
            response.sendRedirect(request.getContextPath() + "/tenant/dashboard?tab=riwayat");
        } catch (Exception e) {
            // Lempar balik ke browse dengan info (biar user tidak stuck)
            response.sendRedirect(request.getContextPath() + "/tenant/browse?info=payment_failed");
        }
    }
}