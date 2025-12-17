package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import model.user;

@WebServlet("/sewaForm")
public class SewaFormServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    user currentUser = (session != null) ? (user) session.getAttribute("currentUser") : null;

    if (currentUser == null) {
      // opsional: simpan tujuan setelah login
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String role = (String) session.getAttribute("role");
    if (!"TENANT".equals(role)) {
      response.sendError(403, "Hanya penyewa yang boleh menyewa properti.");
      return;
    }

    String pid = request.getParameter("propertyId");
    if (pid == null || pid.isBlank()) {
      response.sendError(400, "propertyId wajib ada");
      return;
    }

    request.setAttribute("propertyId", pid);
    request.getRequestDispatcher("/views/sewa/form.jsp").forward(request, response);
  }
}