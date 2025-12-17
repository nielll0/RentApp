package dao;

import model.Property;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {

    // Karena tabel kamu punya kolom: status (bukan is_published)
    // Pastikan value status yang kamu simpan di DB adalah "PUBLISHED"
    // (kalau beda, ganti stringnya di sini)
    private static final String FILTER_PUBLISHED = "UPPER(status) = 'PUBLISHED'";

    public List<Property> searchPublishedLimit(String q, int limit) throws Exception {
        boolean hasQ = q != null && !q.trim().isEmpty();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, judul, alamat, harga_per_bulan, tipe, deskripsi, photo_path ")
           .append("FROM properties ")
           .append("WHERE ").append(FILTER_PUBLISHED).append(" ");

        if (hasQ) {
            sql.append("AND (judul LIKE ? OR alamat LIKE ? OR tipe LIKE ?) ");
        }

        sql.append("ORDER BY id DESC LIMIT ?");

        List<Property> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int idx = 1;

            if (hasQ) {
                String like = "%" + q.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }

            ps.setInt(idx, limit);

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
                    list.add(p);
                }
            }
        }

        return list;
    }
}
