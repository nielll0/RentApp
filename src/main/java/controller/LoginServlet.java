package controller;

import dao.UserDAO;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            UserDAO dao = new UserDAO();
            user userObj = dao.login(email, password);

            if (userObj == null) {
                request.setAttribute("error", "Email atau password salah");
                request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("currentUser", userObj);
            session.setAttribute("role", userObj.getRole());

            String role = userObj.getRole();
            String cp = request.getContextPath();

            // PENTING: redirect ke SERVLET dashboard (bukan JSP)
            // supaya data di-set oleh servlet dan gak kosong sampai refresh.
            if ("TENANT".equals(role)) {
                response.sendRedirect(cp + "/tenant/dashboard");
            } else if ("OWNER".equals(role)) {
                response.sendRedirect(cp + "/owner/dashboard");
            } else if ("ADMIN".equals(role)) {
                response.sendRedirect(cp + "/admin/dashboard");
            } else {
                response.sendRedirect(cp + "/");
            }

        } catch (Exception e) {
            throw new ServletException("Login error: " + e.getMessage(), e);
        }
    }
}