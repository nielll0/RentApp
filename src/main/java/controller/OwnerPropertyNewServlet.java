package controller;

import model.user;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/owner/property/new")
public class OwnerPropertyNewServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        user cu = (session == null) ? null : (user) session.getAttribute("currentUser");
        String role = (session == null) ? null : (String) session.getAttribute("role");

        if (cu == null) { resp.sendRedirect(req.getContextPath() + "/login"); return; }
        if (!"OWNER".equals(role)) { resp.sendError(403); return; }

        req.getRequestDispatcher("/views/properties/new.jsp").forward(req, resp);
    }
}