package com.smartparking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ImageUploadService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String save(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/"))
            throw new RuntimeException("Only image files are allowed");

        if (file.getSize() > 5 * 1024 * 1024)
            throw new RuntimeException("Image must be under 5MB");

        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) Files.createDirectories(path);

        String orig = file.getOriginalFilename();
        String ext = (orig != null && orig.contains("."))
                ? orig.substring(orig.lastIndexOf(".")) : ".jpg";
        String filename = UUID.randomUUID() + ext;

        Files.copy(file.getInputStream(), path.resolve(filename),
                StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
