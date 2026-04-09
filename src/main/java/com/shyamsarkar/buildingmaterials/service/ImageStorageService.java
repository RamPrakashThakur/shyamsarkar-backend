package com.shyamsarkar.buildingmaterials.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String saveImage(MultipartFile file);
}

