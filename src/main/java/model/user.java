package model;

public abstract class user {
    protected int id;
    protected String nama;
    protected String email;
    protected String password;

    // ✅ tambahan untuk fitur admin (suspend/delete)
    protected String status; // ACTIVE / SUSPENDED / DELETED

    public user(int id, String nama, String email, String password) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.status = "ACTIVE"; // default aman
    }

    // ✅ optional constructor kalau kamu butuh isi status saat ambil dari DB
    public user(int id, String nama, String email, String password, String status) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.status = (status == null || status.isBlank()) ? "ACTIVE" : status;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }

    // ✅ getter/setter status
    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = (status == null || status.isBlank()) ? "ACTIVE" : status.trim();
    }

    public abstract String getRole();
}