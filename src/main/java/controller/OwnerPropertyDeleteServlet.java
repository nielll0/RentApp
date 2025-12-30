package controller;

import dao.PropertyDAO;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/owner/property/delete")
public class OwnerPropertyDeleteServlet extends HttpServlet {

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
            new PropertyDAO().delete(id, cu.getId());
            response.sendRedirect(request.getContextPath() + "/owner/dashboard");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}