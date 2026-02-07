package com.nivara.nivarabackend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nivara.nivarabackend.dto.*;
import com.nivara.nivarabackend.entity.Otp;
import com.nivara.nivarabackend.repository.OtpRepository;
import com.nivara.nivarabackend.repository.UserRepository;
import com.nivara.nivarabackend.service.OtpService;
import com.nivara.nivarabackend.service.SmsService;
import com.nivara.nivarabackend.service.EmailService;
import com.nivara.nivarabackend.entity.User;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final SmsService smsService;
    private final EmailService emailService;
    private final UserRepository userRepository;

   public OtpServiceImpl(
            OtpRepository otpRepository,
            SmsService smsService,
            EmailService emailService,
            UserRepository userRepository) {

        this.otpRepository = otpRepository;
        this.smsService = smsService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

   @Override
    @Transactional
    public void sendOtp(OtpRequestDto request) {

        boolean hasMobile = request.getMobileNumber() != null;
        boolean hasEmail = request.getEmail() != null;

        if (hasMobile == hasEmail) {
            throw new RuntimeException("Provide either mobile number or email");
        }

        String generatedOtp =
                String.format("%06d", new Random().nextInt(999999));

        Otp otp = new Otp();
        otp.setOtp(generatedOtp);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setVerified(false);

        if (hasMobile) {
            otp.setMobileNumber(request.getMobileNumber());
            otp.setEmail(null);
            otpRepository.save(otp);
            smsService.sendOtp(request.getMobileNumber(), generatedOtp);
        }

        if (hasEmail) {
            otp.setEmail(request.getEmail());
            otp.setMobileNumber(null);
            otpRepository.save(otp);
            emailService.sendOtpEmail(request.getEmail(), generatedOtp);
        }
    }

    // ---------------- VERIFY OTP ----------------
     @Override
    @Transactional
    public AuthResponseDto verifyOtp(OtpVerifyDto request) {

        boolean hasMobile = request.getMobileNumber() != null;
        boolean hasEmail = request.getEmail() != null;

        if (hasMobile == hasEmail) {
            throw new RuntimeException("Provide either mobile number or email");
        }

        Otp otpEntity;

        if (hasMobile) {
            otpEntity = otpRepository
                .findTopByMobileNumberOrderByCreatedAtDesc(
                    request.getMobileNumber())
                .orElseThrow(() -> new RuntimeException("OTP not found"));
        } else {
            otpEntity = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(
                    request.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not found"));
        }

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!otpEntity.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        otpEntity.setVerified(true);
        otpRepository.save(otpEntity);

        // 🔹 Find or create user
        User user;

        if (hasMobile) {
            user = userRepository.findByPhone(request.getMobileNumber())
                .orElseGet(() -> {
                    User u = new User();
                    u.setPhone(request.getMobileNumber());
                    return userRepository.save(u);
                });
        } else {
            user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail(request.getEmail());
                    return userRepository.save(u);
                });
        }

        // 🔹 TODO: replace with real JWT
        String fakeToken = "sample-jwt-token";

        return new AuthResponseDto(fakeToken,
                hasMobile ? request.getMobileNumber() : request.getEmail());
    }
}
