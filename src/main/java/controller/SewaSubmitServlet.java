/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import model.user;   // sesuaikan kalau class kamu User huruf besar
import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@WebServlet(name = "SewaSubmitServlet", urlPatterns = {"/sewaSubmit"})
public class SewaSubmitServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // kalau orang akses /sewaSubmit lewat GET, arahin ke beranda (atau ke list properti)
        response.sendRedirect(request.getContextPath() + "/");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        user currentUser = (session != null) ? (user) session.getAttribute("currentUser") : null;

        // 1) wajib login
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2) wajib role TENANT
        String role = (String) session.getAttribute("role");
        if (!"TENANT".equals(role)) {
            response.sendError(403, "Hanya penyewa yang boleh menyewa properti.");
            return;
        }

       // 3) ambil & validasi data form
        String propertyIdRaw = request.getParameter("propertyId");
        String tMulaiRaw = request.getParameter("tanggalMulai");
        String tSelesaiRaw = request.getParameter("tanggalSelesai");
        
        System.out.println("propertyId=[" + request.getParameter("propertyId") + "]");
        System.out.println("tanggalMulai=[" + request.getParameter("tanggalMulai") + "]");
        System.out.println("tanggalSelesai=[" + request.getParameter("tanggalSelesai") + "]");

        
        if (propertyIdRaw == null || propertyIdRaw.isBlank() ||
            tMulaiRaw == null || tMulaiRaw.isBlank() ||
            tSelesaiRaw == null || tSelesaiRaw.isBlank()) {
            response.sendError(400, "Parameter form tidak lengkap.");
            return;
        }

        int propertyId;
        LocalDate mulai;
        LocalDate selesai;

        try {
            propertyId = Integer.parseInt(propertyIdRaw);
            mulai = LocalDate.parse(tMulaiRaw);
            selesai = LocalDate.parse(tSelesaiRaw);
        } catch (Exception e) {
            response.sendError(400, "Format parameter tidak valid.");
            return;
        }

        // validasi tanggal sederhana
        if (!selesai.isAfter(mulai)) {
            request.setAttribute("error", "Tanggal selesai harus setelah tanggal mulai.");
            request.setAttribute("propertyId", propertyId);
            request.getRequestDispatcher("views/sewa/form.jsp").forward(request, response);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // 4) ambil harga_per_bulan dari properties
            BigDecimal hargaPerBulan;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT harga_per_bulan FROM properties WHERE id = ? AND status='PUBLISHED'")) {
                ps.setInt(1, propertyId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        response.sendError(404, "Properti tidak ditemukan atau belum dipublish.");
                        return;
                    }
                    hargaPerBulan = rs.getBigDecimal("harga_per_bulan");
                }
            }

            // 5) hitung total biaya (perkiraan)
            long hari = ChronoUnit.DAYS.between(mulai, selesai);
            long bulan = (long) Math.ceil(hari / 30.0); // pembulatan ke atas
            BigDecimal totalBiaya = hargaPerBulan.multiply(BigDecimal.valueOf(bulan));

            // 6) insert ke tabel sewa
            String sqlInsert = "INSERT INTO sewa (property_id, penyewa_id, tanggal_mulai, tanggal_selesai, status, total_biaya) " +
                               "VALUES (?, ?, ?, ?, 'PENDING', ?)";

            try (PreparedStatement psIns = con.prepareStatement(sqlInsert)) {
                psIns.setInt(1, propertyId);
                psIns.setInt(2, currentUser.getId()); // pastikan ada getId()
                psIns.setDate(3, Date.valueOf(mulai));
                psIns.setDate(4, Date.valueOf(selesai));
                psIns.setBigDecimal(5, totalBiaya);

                psIns.executeUpdate();
            }

            // 7) balik ke dashboard penyewa
            response.sendRedirect(request.getContextPath() + "/views/dashboard/penyewa.jsp");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}