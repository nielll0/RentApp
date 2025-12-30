package dao;

import model.SewaRiwayat;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SewaDAO {

    // =========================
    // Tenant: riwayat/pengajuan
    // =========================
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
            ps.setString(2, status == null ? "" : status.trim().toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // ✅ hitung status IN (...)
    public int countByTenantInStatuses(int tenantId, String... statuses) throws SQLException, Exception {
        if (statuses == null || statuses.length == 0) return 0;

        StringBuilder in = new StringBuilder();
        for (int i = 0; i < statuses.length; i++) {
            if (i > 0) in.append(",");
            in.append("?");
        }

        String sql = "SELECT COUNT(*) FROM sewa WHERE penyewa_id=? AND UPPER(status) IN (" + in + ")";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tenantId);

            for (int i = 0; i < statuses.length; i++) {
                String s = (statuses[i] == null) ? "" : statuses[i].trim().toUpperCase();
                ps.setString(2 + i, s);
            }

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

    // =========================
    // Tenant: ambil 1 sewa
    // =========================
    public SewaRiwayat findOneByIdForTenant(int sewaId, int tenantId) throws SQLException, Exception {
        String sql =
                "SELECT s.id AS sewa_id, s.status, s.tanggal_mulai, s.tanggal_selesai, s.total_biaya, " +
                "p.id AS property_id, p.judul, p.alamat, p.harga_per_bulan, p.tipe " +
                "FROM sewa s " +
                "JOIN properties p ON p.id = s.property_id " +
                "WHERE s.id=? AND s.penyewa_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sewaId);
            ps.setInt(2, tenantId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

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

                return r;
            }
        }
    }

    // =========================
    // Dedupe OPEN (status baru)
    // =========================
    public Integer findExistingOpenSewaId(int tenantId, int propertyId) throws SQLException, Exception {
        String sql =
                "SELECT id FROM sewa " +
                "WHERE penyewa_id=? AND property_id=? " +
                "  AND status IN ('REQUESTED','APPROVED','PAID','ACTIVE') " +
                "ORDER BY id DESC LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tenantId);
            ps.setInt(2, propertyId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                return null;
            }
        }
    }

    // =========================
    // Tenant: bayar (APPROVED -> PAID)
    // =========================
    public void markPaidByIdForTenant(int sewaId, int tenantId) throws SQLException, Exception {
        String sql = "UPDATE sewa SET status='PAID' WHERE id=? AND penyewa_id=? AND status='APPROVED'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sewaId);
            ps.setInt(2, tenantId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("Pembayaran ditolak: status sewa harus APPROVED oleh admin dulu.");
            }
        }
    }

    // =========================
    // Admin: list pengajuan REQUESTED
    // =========================
    public List<SewaRiwayat> findAllRequested() throws SQLException, Exception {
        String sql =
                "SELECT s.id AS sewa_id, s.status, s.tanggal_mulai, s.tanggal_selesai, s.total_biaya, " +
                "p.id AS property_id, p.judul, p.alamat, p.harga_per_bulan, p.tipe " +
                "FROM sewa s " +
                "JOIN properties p ON p.id = s.property_id " +
                "WHERE s.status='REQUESTED' " +
                "ORDER BY s.id DESC";

        List<SewaRiwayat> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
        return list;
    }

    public void adminReject(int sewaId) throws SQLException, Exception {
        String sql = "UPDATE sewa SET status='REJECTED' WHERE id=? AND status='REQUESTED'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sewaId);
            int updated = ps.executeUpdate();
            if (updated == 0) throw new SQLException("Reject gagal: sewa tidak ditemukan / status bukan REQUESTED.");
        }
    }

    public void adminApprove(int sewaId) throws SQLException, Exception {
        String sql = "UPDATE sewa SET status='APPROVED' WHERE id=? AND status='REQUESTED'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sewaId);
            int updated = ps.executeUpdate();
            if (updated == 0) throw new SQLException("Approve gagal: sewa tidak ditemukan / status bukan REQUESTED.");
        }
    }

    // =========================
    // OWNER: pengajuan sewa untuk properti milik owner
    // =========================
    public List<SewaRiwayat> findRequestsByOwnerId(int ownerId) throws SQLException, Exception {
        String sql =
                "SELECT s.id AS sewa_id, s.status, s.tanggal_mulai, s.tanggal_selesai, s.total_biaya, " +
                "p.id AS property_id, p.judul, p.alamat, p.harga_per_bulan, p.tipe " +
                "FROM sewa s " +
                "JOIN properties p ON p.id = s.property_id " +
                "WHERE p.pemilik_id = ? " +
                "ORDER BY s.id DESC";

        List<SewaRiwayat> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ownerId);

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

    public int countByOwnerAndStatus(int ownerId, String status) throws SQLException, Exception {
        String sql =
                "SELECT COUNT(*) " +
                "FROM sewa s " +
                "JOIN properties p ON p.id = s.property_id " +
                "WHERE p.pemilik_id=? AND UPPER(s.status)=UPPER(?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ownerId);
            ps.setString(2, status == null ? "" : status.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void ownerApprove(int sewaId, int ownerId) throws SQLException, Exception {
        String sql =
                "UPDATE sewa s " +
                "JOIN properties p ON p.id = s.property_id " +
                "SET s.status='APPROVED' " +
                "WHERE s.id=? AND p.pemilik_id=? AND s.status='REQUESTED'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sewaId);
            ps.setInt(2, ownerId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("Approve gagal: bukan milik owner / status bukan REQUESTED.");
            }
        }
    }

    public void ownerReject(int sewaId, int ownerId) throws SQLException, Exception {
        String sql =
                "UPDATE sewa s " +
                "JOIN properties p ON p.id = s.property_id " +
                "SET s.status='REJECTED' " +
                "WHERE s.id=? AND p.pemilik_id=? AND s.status='REQUESTED'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sewaId);
            ps.setInt(2, ownerId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("Reject gagal: bukan milik owner / status bukan REQUESTED.");
            }
        }
    }
}