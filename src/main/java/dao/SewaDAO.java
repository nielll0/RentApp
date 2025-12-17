package dao;

import model.SewaRiwayat;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SewaDAO {

    public List<SewaRiwayat> findByTenantId(int tenantId) throws SQLException, Exception {
        String sql =
                "SELECT s.id AS sewa_id, s.status, s.tanggal_mulai, s.tanggal_selesai, s.total_biaya, " +
                "p.id AS property_id, p.judul, p.alamat, p.harga_per_bulan, p.tipe " +
                "FROM sewa s " +
                "JOIN properties p ON p.id = s.property_id " +
                "WHERE s.penyewa_id = ? " +
                "ORDER BY s.id DESC";

        List<SewaRiwayat> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tenantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SewaRiwayat r = new SewaRiwayat();
                    r.setSewaId(rs.getInt("sewa_id"));
                    r.setStatus(rs.getString("status"));
                    r.setTanggalMulai(rs.getDate("tanggal_mulai"));
                    r.setTanggalSelesai(rs.getDate("tanggal_selesai"));
                    r.setTotalBiaya(rs.getLong("total_biaya"));

                    r.setPropertyId(rs.getInt("property_id"));
                    r.setJudul(rs.getString("judul"));
                    r.setAlamat(rs.getString("alamat"));
                    r.setHargaPerBulan(rs.getInt("harga_per_bulan"));
                    r.setTipe(rs.getString("tipe"));

                    list.add(r);
                }
            }
        }
        return list;
    }

    public int countByTenantAndStatus(int tenantId, String status) throws SQLException, Exception {
        String sql = "SELECT COUNT(*) FROM sewa WHERE penyewa_id=? AND UPPER(status)=UPPER(?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tenantId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public long sumTotalBiayaByTenant(int tenantId) throws SQLException, Exception {
        String sql = "SELECT COALESCE(SUM(total_biaya),0) FROM sewa WHERE penyewa_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tenantId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0L;
            }
        }
    }
}
