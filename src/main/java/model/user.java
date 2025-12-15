/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public abstract class user {
    protected int id;
    protected String nama;
    protected String email;
    protected String password;

    public user(int id, String nama, String email, String password) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.password = password;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }

    public abstract String getRole();
}