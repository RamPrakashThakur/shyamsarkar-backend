package com.shyamsarkar.buildingmaterials.service;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendDeliveredNotification(String token) {

        try {
            if (token == null || token.isEmpty()) return;

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle("Order Delivered 🎉")
                            .setBody("Your order has been delivered")
                            .build())
                    .build();

            FirebaseMessaging.getInstance().send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}