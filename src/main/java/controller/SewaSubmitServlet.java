package controller;

import model.user;
import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet(name = "SewaSubmitServlet", urlPatterns = {"/tenant/sewa/submit"})
public class SewaSubmitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/tenant/browse");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        user currentUser = (session != null) ? (user) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = currentUser.getRole();
        if (role == null || !"TENANT".equalsIgnoreCase(role.trim())) {
            response.sendError(403, "Hanya penyewa yang boleh mengajukan sewa.");
            return;
        }

        String propertyIdRaw = request.getParameter("propertyId");
        String tMulaiRaw = request.getParameter("tanggalMulai");
        String tSelesaiRaw = request.getParameter("tanggalSelesai");

        if (propertyIdRaw == null || propertyIdRaw.isBlank()
                || tMulaiRaw == null || tMulaiRaw.isBlank()
                || tSelesaiRaw == null || tSelesaiRaw.isBlank()) {
            response.sendError(400, "Parameter form tidak lengkap.");
            return;
        }

        int propertyId;
        LocalDate mulai;
        LocalDate selesai;

        try {
            propertyId = Integer.parseInt(propertyIdRaw.trim());
            mulai = LocalDate.parse(tMulaiRaw.trim());
            selesai = LocalDate.parse(tSelesaiRaw.trim());
        } catch (Exception e) {
            response.sendError(400, "Format parameter tidak valid.");
            return;
        }

        if (!selesai.isAfter(mulai)) {
            request.setAttribute("error", "Tanggal selesai harus setelah tanggal mulai.");
            request.setAttribute("propertyId", propertyId);
            request.getRequestDispatcher("/views/sewa/form.jsp").forward(request, response);
            return;
        }

        // optional: kalau dosen kamu strict, bisa aktifkan ini
        // if (mulai.isBefore(LocalDate.now())) { ... }

        try (Connection con = DBConnection.getConnection()) {

            // 1) Ambil info properti + validasi:
            // - properti published
            // - owner ACTIVE (kalau owner disuspend, tidak bisa disewa)
            BigDecimal hargaPerBulan;
            int kapasitas;

            String sqlProp =
                    "SELECT p.harga_per_bulan, p.kapasitas " +
                    "FROM properties p " +
                    "JOIN users u ON u.id = p.pemilik_id " +
                    "WHERE p.id = ? " +
                    "  AND p.status = 'PUBLISHED' " +
                    "  AND u.role = 'OWNER' " +
                    "  AND u.status = 'ACTIVE'";

            try (PreparedStatement ps = con.prepareStatement(sqlProp)) {
                ps.setInt(1, propertyId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        // Bisa karena: property tidak ada, belum publish, atau owner disuspend/deleted
                        response.sendError(404, "Properti tidak tersedia (mungkin belum publish / owner disuspend).");
                        return;
                    }
                    hargaPerBulan = rs.getBigDecimal("harga_per_bulan");
                    kapasitas = rs.getInt("kapasitas");
                    if (kapasitas < 1) kapasitas = 1; // safety
                }
            }

            // 2) Cek ketersediaan kapasitas pada rentang tanggal (anti overbooking)
            // Yang mengunci kapasitas: APPROVED, PAID, ACTIVE
            String sqlUsed =
                    "SELECT COUNT(*) AS used " +
                    "FROM sewa " +
                    "WHERE property_id = ? " +
                    "  AND status IN ('APPROVED','PAID','ACTIVE') " +
                    "  AND NOT (tanggal_selesai <= ? OR tanggal_mulai >= ?)";

            int used;
            try (PreparedStatement ps = con.prepareStatement(sqlUsed)) {
                ps.setInt(1, propertyId);
                ps.setDate(2, Date.valueOf(mulai));
                ps.setDate(3, Date.valueOf(selesai));
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    used = rs.getInt("used");
                }
            }

            int available = kapasitas - used;
            if (available <= 0) {
                request.setAttribute("error", "Maaf, stok/ketersediaan properti habis untuk tanggal yang kamu pilih.");
                request.setAttribute("propertyId", propertyId);
                request.getRequestDispatcher("/views/sewa/form.jsp").forward(request, response);
                return;
            }

            // 3) Dedupe sederhana: tenant tidak boleh punya pengajuan OPEN untuk properti yang sama
            // OPEN = REQUESTED/APPROVED/PAID/ACTIVE
            String sqlDup =
                    "SELECT id FROM sewa " +
                    "WHERE property_id = ? AND penyewa_id = ? " +
                    "  AND status IN ('REQUESTED','APPROVED','PAID','ACTIVE') " +
                    "ORDER BY id DESC LIMIT 1";

            try (PreparedStatement ps = con.prepareStatement(sqlDup)) {
                ps.setInt(1, propertyId);
                ps.setInt(2, currentUser.getId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int existingId = rs.getInt("id");
                        // Belum ada halaman pengajuan? Nanti kita buat.
                        // Untuk sementara, balik ke browse dengan info.
                        response.sendRedirect(request.getContextPath() + "/tenant/browse?info=already_requested&sewaId=" + existingId);
                        return;
                    }
                }
            }

            // 4) Hitung total biaya (logika kamu tetap dipakai)
            long hari = ChronoUnit.DAYS.between(mulai, selesai);
            long bulan = (long) Math.ceil(hari / 30.0);
            if (bulan < 1) bulan = 1;

            BigDecimal totalBiaya = hargaPerBulan.multiply(BigDecimal.valueOf(bulan));

            // 5) Insert sebagai REQUESTED (bukan PENDING)
            String sqlInsert =
                    "INSERT INTO sewa (property_id, penyewa_id, tanggal_mulai, tanggal_selesai, status, total_biaya) " +
                    "VALUES (?, ?, ?, ?, 'REQUESTED', ?)";

            int sewaId;
            try (PreparedStatement psIns = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                psIns.setInt(1, propertyId);
                psIns.setInt(2, currentUser.getId());
                psIns.setDate(3, Date.valueOf(mulai));
                psIns.setDate(4, Date.valueOf(selesai));
                psIns.setBigDecimal(5, totalBiaya);

                psIns.executeUpdate();

                try (ResultSet keys = psIns.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("Gagal ambil ID sewa.");
                    sewaId = keys.getInt(1);
                }
            }

            // 6) Jangan ke payment dulu. Tunggu admin approve.
            // Kalau halaman pengajuan belum ada, sementara balik ke browse dengan notif.
            response.sendRedirect(request.getContextPath() + "/tenant/browse?success=requested&sewaId=" + sewaId);

        } catch (Exception e) {
            throw new ServletException("Gagal proses pengajuan sewa: " + e.getMessage(), e);
        }
    }
}