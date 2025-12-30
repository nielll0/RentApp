package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.Files;

@WebServlet("/uploads/*")
public class UploadServlet extends HttpServlet {

    private File baseDir;

    @Override
    public void init() throws ServletException {
        // harus sama persis dengan yang dipakai di OwnerPropertySaveServlet
        String uploadDir = System.getProperty("user.home") + File.separator + "rentapp_uploads";
        baseDir = new File(uploadDir);
        if (!baseDir.exists()) {
            boolean ok = baseDir.mkdirs();
            if (!ok) throw new ServletException("Gagal membuat folder upload: " + uploadDir);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // contoh request: /RentApp/uploads/prop_xxx.jpg
        String pathInfo = req.getPathInfo(); // hasilnya: /prop_xxx.jpg

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.contains("..")) {
            resp.sendError(400, "Invalid file");
            return;
        }

        File f = new File(baseDir, pathInfo.substring(1)); // buang "/" depannya
        if (!f.exists() || !f.isFile()) {
            resp.sendError(404);
            return;
        }

        String contentType = Files.probeContentType(f.toPath());
        if (contentType == null) contentType = "application/octet-stream";

        resp.setContentType(contentType);
        resp.setContentLengthLong(f.length());
        resp.setHeader("Cache-Control", "public, max-age=86400");

        try (InputStream in = new FileInputStream(f);
             OutputStream out = resp.getOutputStream()) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = in.read(buf)) != -1) {
                out.write(buf, 0, r);
            }
        }
    }
}