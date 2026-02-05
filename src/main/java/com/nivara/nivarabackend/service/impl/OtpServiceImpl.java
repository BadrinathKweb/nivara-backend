package com.nivara.nivarabackend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nivara.nivarabackend.dto.*;
import com.nivara.nivarabackend.entity.Otp;
import com.nivara.nivarabackend.repository.OtpRepository;
import com.nivara.nivarabackend.service.OtpService;
import com.nivara.nivarabackend.service.SmsService;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final SmsService smsService;

    public OtpServiceImpl(OtpRepository otpRepository,
                          SmsService smsService) {
        this.otpRepository = otpRepository;
        this.smsService = smsService;
    }

    @Override
    @Transactional
    public void sendOtp(OtpRequestDto request) {
        String mobile = request.getMobileNumber();

        String generatedOtp = String.format("%06d", new Random().nextInt(999999));

        Otp otp = new Otp();
        otp.setMobileNumber(mobile);
        otp.setOtp(generatedOtp);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setVerified(false);


        otpRepository.save(otp);

        smsService.sendOtp(mobile, generatedOtp);
        System.out.println("Sending OTP " + generatedOtp + " to " + mobile);
    }

    // ---------------- VERIFY OTP ----------------
    @Override
    @Transactional
    public AuthResponseDto verifyOtp(OtpVerifyDto request) {
        String mobile = request.getMobileNumber();
        String otpInput = request.getOtp();

        // 1️⃣ Fetch latest OTP for the mobile number
        Otp otpEntity = otpRepository.findTopByMobileNumberOrderByCreatedAtDesc(mobile)
                .orElseThrow(() -> new RuntimeException("No OTP sent to this number"));

        // 2️⃣ Check if OTP is expired
        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        // 3️⃣ Check if OTP matches
        if (!otpEntity.getOtp().equals(otpInput)) {
            throw new RuntimeException("Invalid OTP");
        }

        // 4️⃣ Mark OTP as verified
        otpEntity.setVerified(true);
        otpRepository.save(otpEntity);

        // 5️⃣ Create/find user (optional)
        // TODO: implement user creation/login logic

        // 6️⃣ Generate JWT token (optional)
        // TODO: integrate JWT generation
        String fakeToken = "sample-jwt-token";

        // 7️⃣ Return token + mobile
        return new AuthResponseDto(fakeToken, mobile);
    }
}
