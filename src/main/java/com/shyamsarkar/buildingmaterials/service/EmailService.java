package com.shyamsarkar.buildingmaterials.service;

public interface EmailService {
     void sendResetToken(String to, String token);
}
