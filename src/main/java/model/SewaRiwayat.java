/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

public class SewaRiwayat {
    private int sewaId;
    private String status;
    private Date tanggalMulai;
    private Date tanggalSelesai;
    private long totalBiaya;

    private int propertyId;
    private String judul;
    private String alamat;
    private int hargaPerBulan;
    private String tipe;

    public int getSewaId() { return sewaId; }
    public void setSewaId(int sewaId) { this.sewaId = sewaId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(Date tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public Date getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(Date tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }

    public long getTotalBiaya() { return totalBiaya; }
    public void setTotalBiaya(long totalBiaya) { this.totalBiaya = totalBiaya; }

    public int getPropertyId() { return propertyId; }
    public void setPropertyId(int propertyId) { this.propertyId = propertyId; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public int getHargaPerBulan() { return hargaPerBulan; }
    public void setHargaPerBulan(int hargaPerBulan) { this.hargaPerBulan = hargaPerBulan; }

    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }
}
