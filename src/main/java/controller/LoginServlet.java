package controller;

import util.DBConnection;
import model.user;       // <- perbaiki: User huruf besar
import model.Penyewa;
import model.Pemilik;
import model.Admin;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    // GET: kalau buka /login lewat URL atau link, tampilkan halaman login.jsp
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
        // kalau login.jsp tidak di dalam folder views/, ganti jadi "login.jsp"
    }

    // POST: proses form login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND is_active = 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                String role = rs.getString("role");

                // ====== PBO: pakai abstract User + polymorphism ======
                user userObj;
                if ("TENANT".equals(role)) {
                    userObj = new Penyewa(id, nama, email, password);
                } else if ("OWNER".equals(role)) {
                    userObj = new Pemilik(id, nama, email, password);
                } else if ("ADMIN".equals(role)) {
                    userObj = new Admin(id, nama, email, password);
                } else {
                    throw new ServletException("Role tidak dikenal: " + role);
                }

                // simpan di session
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", userObj);
                session.setAttribute("role", role);

                // redirect ke dashboard sesuai role
                String contextPath = request.getContextPath(); // contoh: /RentApp

                if ("TENANT".equals(role)) {
                    response.sendRedirect(contextPath + "/views/dashboard/penyewa.jsp");
                } else if ("OWNER".equals(role)) {
                    response.sendRedirect(contextPath + "/views/dashboard/pemilik.jsp");
                } else { // ADMIN
                    response.sendRedirect(contextPath + "/views/dashboard/admin.jsp");
                }

            } else {
                // kalau email/password salah
                request.setAttribute("error", "Email atau password salah");
                request.getRequestDispatcher("views/login.jsp").forward(request, response);
                // kalau login.jsp tidak di views, ganti path-nya
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
