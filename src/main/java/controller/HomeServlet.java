package controller;

import dao.PropertyDAO;
import model.Property;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/home", "/"})
public class HomeServlet extends HttpServlet {

    private PropertyDAO propertyDAO;
    private static final int LIMIT = 30;

    @Override
    public void init() {
        propertyDAO = new PropertyDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String q = request.getParameter("q");
        if (q != null) {
            q = q.trim();
            if (q.isEmpty()) q = null;
        }

        try {
            List<Property> props = propertyDAO.searchPublishedLimit(q, LIMIT);

            request.setAttribute("q", (q == null) ? "" : q);
            request.setAttribute("properties", props);

            request.getRequestDispatcher("/views/home.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Gagal load landing (SQL): " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ServletException("Gagal load landing: " + e.getMessage(), e);
        }
    }
}