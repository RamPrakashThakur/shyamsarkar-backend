package com.shyamsarkar.buildingmaterials.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;

import com.shyamsarkar.buildingmaterials.service.ImageStorageService;
@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final List<String> ALLOWED_TYPES =
            List.of("image/jpeg", "image/png");

    private static final Logger logger = LoggerFactory.getLogger(ImageStorageService.class);

    

    @Override
public String saveImage(MultipartFile file) {
    try {
        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Could not create directory: " + uploadDir);
        }

        String filename =
                System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File dest = new File(dir, filename);
        file.transferTo(dest);

        return "/images/" + filename;

    } catch (IOException e) {
        logger.error("IMAGE SAVE FAILED", e); // 🔥 IMPORTANT
        throw new RuntimeException("Could not store image", e);
    }
}

    private String getExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }

        return filename.substring(filename.lastIndexOf("."));
    }
}

