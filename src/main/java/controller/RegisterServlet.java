package controller;

import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // tampilkan halaman register
        request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response); // pastikan path register.jsp bener
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nama = request.getParameter("nama");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String noKontak = request.getParameter("no_kontak");
        String role = request.getParameter("role"); // TENANT / OWNER

        if (isBlank(nama) || isBlank(email) || isBlank(password) || isBlank(noKontak) || isBlank(role)) {
            request.setAttribute("error", "Semua field wajib diisi.");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }

        role = role.trim().toUpperCase();
        if (!role.equals("TENANT") && !role.equals("OWNER")) {
            request.setAttribute("error", "Role tidak valid.");
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // 1) cek email sudah ada?
            try (PreparedStatement cek = con.prepareStatement("SELECT id FROM users WHERE email=? LIMIT 1")) {
                cek.setString(1, email.trim());
                try (ResultSet rs = cek.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("error", "Email sudah terdaftar, silakan login.");
                        request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
                        return;
                    }
                }
            }

            // 2) insert
            String sql = "INSERT INTO users(nama,email,password,no_kontak,role) VALUES(?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nama.trim());
                ps.setString(2, email.trim());
                ps.setString(3, password); // (kalau mau aman: hash)
                ps.setString(4, noKontak.trim());
                ps.setString(5, role);
                ps.executeUpdate();
            }

            // ✅ IMPORTANT: redirect ke ROUTE /login (bukan ke /views/login.jsp)
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception e) {
            throw new ServletException("Gagal register: " + e.getMessage(), e);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}