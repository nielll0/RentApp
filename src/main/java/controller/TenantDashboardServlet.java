package controller;

import model.user;
import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/tenant/dashboard")
public class TenantDashboardServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    user u = (session != null) ? (user) session.getAttribute("currentUser") : null;
    String role = (session != null) ? (String) session.getAttribute("role") : null;

    if (u == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }
    if (!"TENANT".equals(role)) {
      response.sendError(403, "Halaman ini khusus penyewa.");
      return;
    }

    try (Connection con = DBConnection.getConnection()) {
      String sql =
        "SELECT s.id AS sewa_id, s.status, s.tanggal_mulai, s.tanggal_selesai, s.total_biaya, " +
        "p.id AS property_id, p.judul, p.alamat, p.harga_per_bulan, p.tipe, p.deskripsi " +
        "FROM sewa s JOIN properties p ON p.id = s.property_id " +
        "WHERE s.penyewa_id = ? ORDER BY s.id DESC";
      try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, u.getId());
        ResultSet rs = ps.executeQuery();
        request.setAttribute("rs", rs);
        request.getRequestDispatcher("/views/dashboard/penyewa.jsp").forward(request, response);
      }
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}
