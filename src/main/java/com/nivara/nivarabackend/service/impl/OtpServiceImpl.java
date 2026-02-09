package com.nivara.nivarabackend.service.impl;

import com.nivara.nivarabackend.config.JwtUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nivara.nivarabackend.dto.*;
import com.nivara.nivarabackend.entity.OtpType;
import com.nivara.nivarabackend.entity.OtpVerification;
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
    private final JwtUtil jwtUtil;

    public OtpServiceImpl(
            OtpRepository otpRepository,
            SmsService smsService,
            EmailService emailService,
            UserRepository userRepository,
            JwtUtil jwtUtil) {

        this.otpRepository = otpRepository;
        this.smsService = smsService;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void sendMobileOtp(String mobile) {

        String otp = generateOtp();

        OtpVerification entity = new OtpVerification();
        entity.setIdentifier(mobile);
        entity.setType(OtpType.MOBILE);
        entity.setOtpCode(otp);
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        entity.setVerified(false);

        otpRepository.save(entity);

        System.err.println(otp);
        //smsService.sendOtp(mobile, otp);
    }

    @Override
    @Transactional
    public AuthResponseDto verifyMobileOtp(String mobile, String otp) {

        OtpVerification entity = otpRepository
                .findTopByIdentifierAndTypeAndVerifiedFalseOrderByIdDesc(
                        mobile, OtpType.MOBILE)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        validateOtp(entity, otp);

        entity.setVerified(true);

        User user = userRepository.findByMobileNumber(mobile)
                .orElseGet(() -> {
                    User u = new User();
                    u.setMobileNumber(mobile);
                    u.setMobileVerified(true);
                    return userRepository.save(u);
                });

        String token = jwtUtil.generateToken(user.getId());

        return new AuthResponseDto(
                token,
                user.isEmailVerified(),
                false
        );
    }

    @Override
    public void sendEmailOtp(Long userId, String email) {

        userRepository.findByEmail(email)
                .ifPresent(u -> {
                    throw new RuntimeException("Email already exists");
                });

        String otp = generateOtp();

        OtpVerification entity = new OtpVerification();
        entity.setIdentifier(email);
        entity.setType(OtpType.EMAIL);
        entity.setOtpCode(otp);
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        entity.setVerified(false);

        otpRepository.save(entity);
        System.out.println("Email : "+otp);

        emailService.sendOtpEmail(email, otp);
    }

    @Override
    @Transactional
    public void verifyEmailOtp(Long userId, String email, String otp) {

        OtpVerification entity = otpRepository
                .findTopByIdentifierAndTypeAndVerifiedFalseOrderByIdDesc(
                        email, OtpType.EMAIL)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        validateOtp(entity, otp);

        entity.setVerified(true);

        User user = userRepository.findById(userId).orElseThrow();

        user.setEmail(email);
        user.setEmailVerified(true);
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(9000) + 1000);
    }

    private void validateOtp(OtpVerification entity, String otp) {

        if (!entity.getOtpCode().equals(otp))
            throw new RuntimeException("Invalid OTP");

        if (entity.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");
    }
}

