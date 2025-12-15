/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name="HomeServlet", urlPatterns={"/"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String q = request.getParameter("q");
        if (q == null) q = "";

        try (Connection con = DBConnection.getConnection()) {
            String sql =
                "SELECT id, judul, alamat, harga_per_bulan, tipe " +
                "FROM properties " +
                "WHERE status='PUBLISHED' AND (judul LIKE ? OR alamat LIKE ? OR tipe LIKE ?) " +
                "ORDER BY created_at DESC";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                String like = "%" + q + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);

                ResultSet rs = ps.executeQuery();
                request.setAttribute("rs", rs);
                request.setAttribute("q", q);

                request.getRequestDispatcher("views/home.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
