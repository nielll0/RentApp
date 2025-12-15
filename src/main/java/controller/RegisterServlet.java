package controller;

import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    // GET: kalau user buka /register langsung, tampilkan form register
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("views/auth/register.jsp").forward(request, response);
        // kalau register.jsp di root, ganti jadi "register.jsp"
    }

    // POST: proses form registrasi
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nama = request.getParameter("nama");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String noKontak = request.getParameter("no_kontak");
        String role = request.getParameter("role"); // TENANT / OWNER

        try (Connection con = DBConnection.getConnection()) {

            String sql = "INSERT INTO users (nama, email, password, no_kontak, role, is_active) " +
                         "VALUES (?, ?, ?, ?, ?, 1)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, noKontak);
            ps.setString(5, role);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                // berhasil daftar → redirect ke login biar form tidak ke-submit dua kali
                String contextPath = request.getContextPath();
                response.sendRedirect(contextPath + "/views/login.jsp");
                // kalau mau tetap pakai forward + message, boleh, tapi hati2 F5 ngulang insert
            } else {
                request.setAttribute("error", "Registrasi gagal, silakan coba lagi.");
                request.getRequestDispatcher("views/register.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Terjadi error: " + e.getMessage());
            request.getRequestDispatcher("views/register.jsp").forward(request, response);
        }
    }
}
