package com.nivara.nivarabackend.service;

public interface EmailService {

    void sendOtpEmail(String toEmail, String otp);
}
