/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Penyewa extends user {
    public Penyewa(int id, String nama, String email, String password) {
        super(id, nama, email, password);
    }

    @Override
    public String getRole() {
        return "TENANT";
    }
}

