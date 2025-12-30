package dao;

import model.user;
import model.Penyewa;
import model.Pemilik;
import model.Admin;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Login user aktif berdasarkan email+password.
     * NOTE: kamu masih pakai is_active=1, itu boleh.
     * Tapi untuk fitur suspend/delete, sebaiknya sinkron dengan kolom status juga.
     */
    public user login(String email, String password) throws Exception {
        // ✅ Aku tambah guard status biar owner yang SUSPENDED/DELETED gak bisa login
        // Kalau kamu belum punya kolom status, hapus bagian AND status='ACTIVE'
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND is_active = 1 AND status='ACTIVE'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                String role = rs.getString("role");
                String status = rs.getString("status");

                // polymorphism sesuai role (PBO)
                if ("TENANT".equals(role)) {
                    Penyewa p = new Penyewa(id, nama, email, password);
                    p.setStatus(status);
                    return p;
                } else if ("OWNER".equals(role)) {
                    Pemilik o = new Pemilik(id, nama, email, password);
                    o.setStatus(status);
                    return o;
                } else if ("ADMIN".equals(role)) {
                    Admin a = new Admin(id, nama, email, password);
                    a.setStatus(status);
                    return a;
                } else {
                    return null;
                }
            }
        }
    }

    // ✅ dipakai admin untuk list owner
    public List<user> findOwners() throws Exception {
        String sql = "SELECT id, nama, email, password, role, status FROM users WHERE role='OWNER' ORDER BY id DESC";
        List<user> list = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                String email = rs.getString("email");
                String pass = rs.getString("password");
                String status = rs.getString("status");

                Pemilik owner = new Pemilik(id, nama, email, pass);
                owner.setStatus(status);
                list.add(owner);
            }
        }
        return list;
    }

    // ✅ dipakai admin untuk suspend/activate/delete (soft)
    public void updateUserStatus(int ownerId, String status) throws Exception {
        String sql = "UPDATE users SET status=? WHERE id=? AND role='OWNER'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, ownerId);

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new Exception("Owner tidak ditemukan / bukan role OWNER.");
            }
        }
    }
}