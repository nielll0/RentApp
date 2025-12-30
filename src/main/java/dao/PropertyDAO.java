package dao;

import model.Property;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAO {

    private Connection getConnection() throws SQLException, Exception {
        return util.DBConnection.getConnection();
    }

    // =========================
    // INSERT
    // =========================
    public void insert(Property p) throws SQLException, Exception {
        String sql = "INSERT INTO properties (judul, alamat, harga_per_bulan, tipe, deskripsi, pemilik_id, status, photo_path) " +
                     "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getJudul());
            ps.setString(2, p.getAlamat());
            ps.setDouble(3, p.getHargaPerBulan());
            ps.setString(4, p.getTipe());
            ps.setString(5, p.getDeskripsi());
            ps.setInt(6, p.getPemilikId());
            ps.setString(7, p.getStatus());
            ps.setString(8, p.getPhotoPath());

            ps.executeUpdate();
        }
    }

    // =========================
    // LIST PROPERTY BY PEMILIK
    // =========================
    public List<Property> getByPemilik(int pemilikId) throws SQLException, Exception {
        String sql = "SELECT id, judul, alamat, harga_per_bulan, tipe, deskripsi, pemilik_id, status, photo_path " +
                     "FROM properties WHERE pemilik_id=? ORDER BY id DESC";
        List<Property> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, pemilikId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    // =========================
    // GET BY ID + PEMILIK
    // =========================
    public Property getByIdAndPemilik(int id, int pemilikId) throws SQLException, Exception {
        String sql = "SELECT id, judul, alamat, harga_per_bulan, tipe, deskripsi, pemilik_id, status, photo_path " +
                     "FROM properties WHERE id=? AND pemilik_id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, pemilikId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    // =========================
    // UPDATE
    // =========================
    public boolean update(Property p) throws SQLException, Exception {
        String sql = "UPDATE properties SET judul=?, alamat=?, harga_per_bulan=?, tipe=?, deskripsi=?, status=?, photo_path=? " +
                     "WHERE id=? AND pemilik_id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getJudul());
            ps.setString(2, p.getAlamat());
            ps.setDouble(3, p.getHargaPerBulan());
            ps.setString(4, p.getTipe());
            ps.setString(5, p.getDeskripsi());
            ps.setString(6, p.getStatus());
            ps.setString(7, p.getPhotoPath());
            ps.setInt(8, p.getId());
            ps.setInt(9, p.getPemilikId());

            return ps.executeUpdate() > 0;
        }
    }

    // =========================
    // DELETE
    // =========================
    public boolean delete(int id, int pemilikId) throws SQLException, Exception {
        String sql = "DELETE FROM properties WHERE id=? AND pemilik_id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, pemilikId);
            return ps.executeUpdate() > 0;
        }
    }

    // =========================
    // LANDING: published + search limit
    // =========================
    public List<Property> searchPublishedLimit(String q, int limit) throws SQLException, Exception {
        boolean hasQ = (q != null && !q.trim().isEmpty());

        String base = "SELECT id, judul, alamat, harga_per_bulan, tipe, deskripsi, pemilik_id, status, photo_path " +
                      "FROM properties WHERE status='PUBLISHED' ";
        String filter = hasQ ? "AND (judul LIKE ? OR alamat LIKE ? OR tipe LIKE ?) " : "";
        String sql = base + filter + "ORDER BY id DESC LIMIT ?";

        List<Property> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            int idx = 1;
            if (hasQ) {
                String like = "%" + q.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            ps.setInt(idx, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    private Property map(ResultSet rs) throws SQLException {
        Property p = new Property();
        p.setId(rs.getInt("id"));
        p.setJudul(rs.getString("judul"));
        p.setAlamat(rs.getString("alamat"));
        p.setHargaPerBulan(rs.getDouble("harga_per_bulan"));
        p.setTipe(rs.getString("tipe"));
        p.setDeskripsi(rs.getString("deskripsi"));
        p.setPemilikId(rs.getInt("pemilik_id"));
        p.setStatus(rs.getString("status"));
        p.setPhotoPath(rs.getString("photo_path"));
        return p;
    }
}