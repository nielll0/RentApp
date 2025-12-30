package controller;

import dao.PropertyDAO;
import model.Property;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/owner/property/edit")
public class OwnerPropertyEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        user cu = (session == null) ? null : (user) session.getAttribute("currentUser");
        String role = (session == null) ? null : (String) session.getAttribute("role");

        if (cu == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
        if (!"OWNER".equals(role)) { response.sendError(403); return; }

        int id;
        try { id = Integer.parseInt(request.getParameter("id")); }
        catch (Exception e) { response.sendError(400, "id tidak valid"); return; }

        try {
            Property p = new PropertyDAO().getByIdAndPemilik(id, cu.getId());
            if (p == null) { response.sendError(404, "Property tidak ditemukan"); return; }

            request.setAttribute("property", p);
            request.getRequestDispatcher("/views/owner_property_edit.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}