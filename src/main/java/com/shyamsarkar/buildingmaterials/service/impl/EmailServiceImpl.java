package com.shyamsarkar.buildingmaterials.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.shyamsarkar.buildingmaterials.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {


    @Autowired(required = false)
    private  JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetToken(String to, String token) {


        if (mailSender == null) {
            System.out.println("Mail service not configured");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Token");
        message.setText(
                "Your password reset token is:\n\n"
                        + token
                        + "\n\nToken valid for 15 minutes."
        );

        mailSender.send(message);
    }
}
