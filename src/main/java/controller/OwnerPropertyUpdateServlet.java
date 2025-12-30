package controller;

import dao.PropertyDAO;
import model.Property;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@MultipartConfig
@WebServlet("/owner/property/update")
public class OwnerPropertyUpdateServlet extends HttpServlet {

    private File uploadBaseDir;

    @Override
    public void init() throws ServletException {
        String basePath = System.getProperty("user.home") + File.separator + "rentapp_uploads";
        uploadBaseDir = new File(basePath);
        if (!uploadBaseDir.exists() && !uploadBaseDir.mkdirs()) {
            throw new ServletException("Gagal membuat folder upload: " + basePath);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        user cu = (session == null) ? null : (user) session.getAttribute("currentUser");
        String role = (session == null) ? null : (String) session.getAttribute("role");

        if (cu == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
        if (!"OWNER".equals(role)) { response.sendError(403); return; }

        int id;
        try { id = Integer.parseInt(request.getParameter("id")); }
        catch (Exception e) { response.sendError(400, "id tidak valid"); return; }

        String judul = trim(request.getParameter("judul"));
        String alamat = trim(request.getParameter("alamat"));
        String hargaStr = trim(request.getParameter("harga_per_bulan"));
        String tipe = trim(request.getParameter("tipe"));
        String deskripsi = trim(request.getParameter("deskripsi"));
        String action = trim(request.getParameter("action")); // DRAFT / PUBLISHED

        if (isBlank(judul) || isBlank(alamat) || isBlank(hargaStr) || isBlank(tipe)) {
            response.sendError(400, "Field wajib tidak boleh kosong");
            return;
        }

        double harga;
        try { harga = Double.parseDouble(hargaStr); }
        catch (Exception e) { response.sendError(400, "Harga tidak valid"); return; }

        String status = ("PUBLISHED".equalsIgnoreCase(action)) ? "PUBLISHED" : "DRAFT";

        try {
            PropertyDAO dao = new PropertyDAO();
            Property existing = dao.getByIdAndPemilik(id, cu.getId());
            if (existing == null) { response.sendError(404, "Property tidak ditemukan"); return; }

            // upload foto opsional: kalau tidak upload, pakai foto lama
            Part photoPart = request.getPart("photo");
            String photoPath = existing.getPhotoPath();

            if (photoPart != null && photoPart.getSize() > 0) {
                String original = Paths.get(photoPart.getSubmittedFileName()).getFileName().toString();
                String ext = getExt(original);

                if (!ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".png") && !ext.equals(".webp")) {
                    response.sendError(400, "File foto harus jpg/jpeg/png/webp");
                    return;
                }

                String newName = "prop_" + System.currentTimeMillis() + "_" +
                        UUID.randomUUID().toString().replace("-", "") + ext;

                File saved = new File(uploadBaseDir, newName);
                photoPart.write(saved.getAbsolutePath());

                photoPath = "/uploads/" + newName;
            }

            Property p = new Property();
            p.setId(id);
            p.setPemilikId(cu.getId());
            p.setJudul(judul);
            p.setAlamat(alamat);
            p.setHargaPerBulan(harga);
            p.setTipe(tipe);
            p.setDeskripsi(deskripsi);
            p.setStatus(status);
            p.setPhotoPath(photoPath);

            dao.update(p);

            response.sendRedirect(request.getContextPath() + "/owner/dashboard");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private static String trim(String s){ return s==null? null : s.trim(); }
    private static boolean isBlank(String s){ return s==null || s.trim().isEmpty(); }
    private static String getExt(String filename){
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        if (dot < 0) return "";
        return filename.substring(dot).toLowerCase();
    }
}