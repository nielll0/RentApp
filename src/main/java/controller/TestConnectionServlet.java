/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import util.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="TestConnectionServlet", urlPatterns={"/test-db"})
public class TestConnectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        try (PrintWriter out = resp.getWriter()) {
            try (Connection conn = DBConnection.getConnection()) {
                out.println("KONEKSI BERHASIL! ✔");
                out.println("Conn: " + conn);
            } catch (Exception e) {
                out.println("KONEKSI GAGAL ❌");
                e.printStackTrace(out);
            }
        }
    }
}