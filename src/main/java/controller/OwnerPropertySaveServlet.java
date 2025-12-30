package controller;

import dao.PropertyDAO;
import model.Property;
import model.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1MB buffer
        maxFileSize = 5L * 1024 * 1024,       // 5MB per file
        maxRequestSize = 6L * 1024 * 1024     // 6MB total request
)
@WebServlet("/owner/property/save")
public class OwnerPropertySaveServlet extends HttpServlet {

    private File uploadBaseDir;

    @Override
    public void init() throws ServletException {
        ensureUploadDir();
        System.out.println("[OwnerPropertySaveServlet] Upload base dir = " + uploadBaseDir.getAbsolutePath());
    }

    private void ensureUploadDir() throws ServletException {
        String basePath = System.getProperty("user.home") + File.separator + "rentapp_uploads";
        uploadBaseDir = new File(basePath);

        if (!uploadBaseDir.exists()) {
            boolean ok = uploadBaseDir.mkdirs();
            if (!ok) throw new ServletException("Gagal membuat folder upload: " + basePath);
        }

        if (!uploadBaseDir.isDirectory()) {
            throw new ServletException("Upload path bukan folder: " + basePath);
        }

        if (!uploadBaseDir.canWrite()) {
            throw new ServletException("Folder upload tidak writable: " + basePath);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1) auth (FIX: jangan pakai session attribute "role" yang bisa null)
        HttpSession session = request.getSession(false);
        user cu = (session == null) ? null : (user) session.getAttribute("currentUser");

        if (cu == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (cu.getRole() == null || !"OWNER".equalsIgnoreCase(cu.getRole().trim())) {
            response.sendError(403, "Hanya OWNER yang boleh akses.");
            return;
        }

        // pastikan folder upload siap
        ensureUploadDir();

        // 2) ambil input
        String judul = safeTrim(request.getParameter("judul"));
        String alamat = safeTrim(request.getParameter("alamat"));
        String hargaStr = safeTrim(request.getParameter("harga_per_bulan"));
        String tipe = safeTrim(request.getParameter("tipe"));
        String deskripsi = safeTrim(request.getParameter("deskripsi"));
        String action = safeTrim(request.getParameter("action")); // sekarang cuma PUBLISHED dari JSP

        if (isBlank(judul) || isBlank(alamat) || isBlank(hargaStr) || isBlank(tipe)) {
            response.sendError(400, "Field wajib kosong");
            return;
        }

        double harga;
        try {
            harga = Double.parseDouble(hargaStr);
            if (harga < 0) throw new NumberFormatException("harga < 0");
        } catch (Exception e) {
            response.sendError(400, "Harga tidak valid");
            return;
        }

        // Karena tombol draft dihapus, default kita PUBLISHED (tetap aman kalau action null)
        String status = ("PUBLISHED".equalsIgnoreCase(action)) ? "PUBLISHED" : "PUBLISHED";

        // 3) upload foto (opsional)
        Part photoPart = request.getPart("photo");
        String photoPath = null; // disimpan ke DB: "/uploads/<nama file>"

        if (photoPart != null && photoPart.getSize() > 0) {
            String submitted = photoPart.getSubmittedFileName();
            String original = (submitted == null) ? "" : Paths.get(submitted).getFileName().toString();
            String ext = getExtensionLower(original);

            if (!ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".png") && !ext.equals(".webp")) {
                response.sendError(400, "File foto harus jpg/jpeg/png/webp");
                return;
            }

            String newName = "prop_" + System.currentTimeMillis() + "_" +
                    UUID.randomUUID().toString().replace("-", "") + ext;

            File saved = new File(uploadBaseDir, newName);

            try (InputStream in = photoPart.getInputStream();
                 OutputStream out = new FileOutputStream(saved)) {
                byte[] buf = new byte[8192];
                int r;
                while ((r = in.read(buf)) != -1) {
                    out.write(buf, 0, r);
                }
            }

            if (!saved.exists() || saved.length() <= 0) {
                throw new ServletException("Upload gagal tersimpan: " + saved.getAbsolutePath());
            }

            System.out.println("[OwnerPropertySaveServlet] Saved photo: " + saved.getAbsolutePath()
                    + " (" + saved.length() + " bytes)");

            photoPath = "/uploads/" + newName;
        } else {
            System.out.println("[OwnerPropertySaveServlet] No photo uploaded (photoPart null/size=0)");
        }

        // 4) simpan ke DB via DAO
        try {
            Property p = new Property();
            p.setJudul(judul);
            p.setAlamat(alamat);
            p.setHargaPerBulan(harga);
            p.setTipe(tipe);
            p.setDeskripsi(deskripsi);
            p.setPemilikId(cu.getId());
            p.setStatus(status);
            p.setPhotoPath(photoPath);

            new PropertyDAO().insert(p);

            response.sendRedirect(request.getContextPath() + "/owner/dashboard");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private static String safeTrim(String s) {
        return (s == null) ? null : s.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String getExtensionLower(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        if (dot < 0) return "";
        return filename.substring(dot).toLowerCase();
    }
}