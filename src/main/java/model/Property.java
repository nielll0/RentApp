package model;

public class Property {

    private int id;
    private int pemilikId;

    private String judul;
    private String alamat;
    private double hargaPerBulan;
    private String tipe;
    private String deskripsi;

    private String status;
    private String photoPath;

    // ===== constructor kosong =====
    public Property() {}

    // ===== getter & setter =====
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPemilikId() { return pemilikId; }
    public void setPemilikId(int pemilikId) { this.pemilikId = pemilikId; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public double getHargaPerBulan() { return hargaPerBulan; }
    public void setHargaPerBulan(double hargaPerBulan) { this.hargaPerBulan = hargaPerBulan; }

    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
}
