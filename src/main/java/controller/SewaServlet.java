/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.user; // atau model.User sesuai class kamu
import util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet(name="SewaServlet", urlPatterns={"/sewa"})
public class SewaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // cek login
        HttpSession session = request.getSession(false);
        user currentUser = (session != null) ? (user) session.getAttribute("currentUser") : null;

        // kalau belum login → lempar ke login
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // cek role harus TENANT
        String role = (String) session.getAttribute("role");
        if (!"TENANT".equals(role)) {
            response.sendError(403, "Hanya penyewa yang boleh menyewa properti.");
            return;
        }

        // ambil propertyId dari query param
        String propertyId = request.getParameter("propertyId");
        request.setAttribute("propertyId", propertyId);

        // tampilkan form sewa
        request.getRequestDispatcher("views/sewa/form.jsp").forward(request, response);
    }
}