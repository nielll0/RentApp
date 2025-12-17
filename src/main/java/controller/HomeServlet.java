package controller;

import model.Property;
import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/", "/home"})
public class HomeServlet extends HttpServlet {

    private static final int LIMIT = 12;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // query search optional
        String q = request.getParameter("q");
        if (q != null) {
            q = q.trim();
            if (q.isEmpty()) q = null;
        }

        List<Property> props = new ArrayList<>();

        String sql =
            "SELECT id, judul, alamat, harga_per_bulan, tipe, deskripsi, photo_path " +
            "FROM properties " +
            "WHERE UPPER(status) = 'PUBLISHED' " +
            (q != null ? "AND (judul LIKE ? OR alamat LIKE ? OR tipe LIKE ?) " : "") +
            "ORDER BY created_at DESC " +
            "LIMIT ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int idx = 1;

            if (q != null) {
                String like = "%" + q + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }

            ps.setInt(idx, LIMIT);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Property p = new Property();
                    p.setId(rs.getInt("id"));
                    p.setJudul(rs.getString("judul"));
                    p.setAlamat(rs.getString("alamat"));
                    p.setHargaPerBulan(rs.getInt("harga_per_bulan"));
                    p.setTipe(rs.getString("tipe"));
                    p.setDeskripsi(rs.getString("deskripsi"));
                    p.setPhotoPath(rs.getString("photo_path"));
                    props.add(p);
                }
            }

            // kirim ke JSP
            request.setAttribute("q", q);                 // bisa null
            request.setAttribute("properties", props);    // LIST, bukan ResultSet
            request.getRequestDispatcher("/views/home.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Gagal load Home: " + e.getMessage(), e);
        }
    }
}