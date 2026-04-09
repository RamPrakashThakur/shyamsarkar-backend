package com.shyamsarkar.buildingmaterials.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class ImageController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(
            @PathVariable String filename) {

        try {
            Path file = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            MediaType mediaType = getMediaType(filename);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private MediaType getMediaType(String filename) {
        String lower = filename.toLowerCase();

        if (lower.endsWith(".png")) return MediaType.IMAGE_PNG;
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg"))
            return MediaType.IMAGE_JPEG;

        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
