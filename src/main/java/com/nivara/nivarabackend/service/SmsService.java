package com.nivara.nivarabackend.service;

public interface SmsService {

    void sendOtp(String mobile, String otp);
}
