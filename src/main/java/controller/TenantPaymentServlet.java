package controller;

import dao.SewaDAO;
import model.SewaRiwayat;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/tenant/payment")
public class TenantPaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
            response.sendError(403, "Hanya penyewa yang boleh akses pembayaran.");
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
            SewaRiwayat sewa = new SewaDAO().findOneByIdForTenant(sewaId, u.getId());
            if (sewa == null) {
                response.sendError(404, "Data sewa tidak ditemukan");
                return;
            }

            String status = (sewa.getStatus() == null) ? "" : sewa.getStatus().trim().toUpperCase();

            // GUARD sesuai rule bisnis
            if (!"APPROVED".equals(status)) {
                // Arahkan ke halaman yang lebih cocok.
                // Kalau kamu belum punya halaman pengajuan, sementara lempar ke browse.
                String msg;
                switch (status) {
                    case "REQUESTED":
                        msg = "Pengajuan kamu masih menunggu persetujuan admin.";
                        break;
                    case "REJECTED":
                        msg = "Pengajuan kamu ditolak admin.";
                        break;
                    case "PAID":
                    case "ACTIVE":
                    case "SELESAI":
                        msg = "Sewa ini sudah dibayar/berjalan/selesai. Tidak bisa dibayar lagi.";
                        break;
                    default:
                        msg = "Status sewa tidak valid untuk pembayaran: " + status;
                }
                response.sendRedirect(request.getContextPath() + "/tenant/browse?info=" + encode(msg));
                return;
            }

            // Status APPROVED -> boleh bayar
            request.setAttribute("sewa", sewa);
            request.getRequestDispatcher("/views/sewa/Payment.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Gagal load payment: " + e.getMessage(), e);
        }
    }

    // Simple encoder biar tidak rusak query string (tanpa lib tambahan)
    private String encode(String s) {
        return s == null ? "" : s.replace(" ", "%20");
    }
}