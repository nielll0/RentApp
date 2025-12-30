package controller;

import dao.SewaDAO;
import model.SewaRiwayat;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/owner/rentals")
public class OwnerRentalsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        user u = (session != null) ? (user) session.getAttribute("currentUser") : null;

        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (u.getRole() == null || !"OWNER".equalsIgnoreCase(u.getRole().trim())) {
            response.sendError(403, "Hanya OWNER yang boleh akses halaman ini.");
            return;
        }

        try {
            SewaDAO dao = new SewaDAO();

            List<SewaRiwayat> list = dao.findRequestsByOwnerId(u.getId());

            int requested = dao.countByOwnerAndStatus(u.getId(), "REQUESTED");
            int approved  = dao.countByOwnerAndStatus(u.getId(), "APPROVED");
            int paid      = dao.countByOwnerAndStatus(u.getId(), "PAID");
            int rejected  = dao.countByOwnerAndStatus(u.getId(), "REJECTED");

            request.setAttribute("rentals", list);
            request.setAttribute("countRequested", requested);
            request.setAttribute("countApproved", approved);
            request.setAttribute("countPaid", paid);
            request.setAttribute("countRejected", rejected);

            request.getRequestDispatcher("/views/dashboard/pemilik_rentals.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Gagal load pengajuan sewa owner: " + e.getMessage(), e);
        }
    }
}