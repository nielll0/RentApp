package model;

public class Property {
    private int id;
    private String judul;
    private String alamat;
    private int hargaPerBulan;
    private String tipe;
    private String deskripsi;
    private String photoPath;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public int getHargaPerBulan() { return hargaPerBulan; }
    public void setHargaPerBulan(int hargaPerBulan) { this.hargaPerBulan = hargaPerBulan; }

    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
}
