package com.nivara.nivarabackend.service.impl;

import com.nivara.nivarabackend.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for Nivara");
        message.setText(
            "Your OTP is: " + otp +
            "\n\nThis OTP is valid for 5 minutes." +
            "\n\nIf you didn’t request this, please ignore."
        );

        mailSender.send(message);
    }
}
