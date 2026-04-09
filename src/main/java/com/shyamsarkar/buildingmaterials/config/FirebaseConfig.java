package com.shyamsarkar.buildingmaterials.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            String json = System.getenv("GOOGLE_CREDENTIALS");

            if (json == null || json.isEmpty()) {
                throw new RuntimeException("GOOGLE_CREDENTIALS not found");
            }

            InputStream stream = new ByteArrayInputStream(
                    json.getBytes(StandardCharsets.UTF_8)
            );
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
