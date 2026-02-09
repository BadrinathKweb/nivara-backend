package com.nivara.nivarabackend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.nivara.nivarabackend.config.JwtUtil;
import com.nivara.nivarabackend.dto.ApiResponseDto;
import com.nivara.nivarabackend.dto.AuthResponseDto;
import com.nivara.nivarabackend.dto.EmailOtpRequestDto;
import com.nivara.nivarabackend.dto.EmailOtpVerifyDto;
import com.nivara.nivarabackend.dto.GoogleLoginRequest;
import com.nivara.nivarabackend.dto.MobileOtpRequestDto;
import com.nivara.nivarabackend.dto.MobileOtpVerifyDto;
import com.nivara.nivarabackend.dto.OtpVerifyDto;
import com.nivara.nivarabackend.entity.User;
import com.nivara.nivarabackend.repository.UserRepository;
import com.nivara.nivarabackend.service.OtpService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/nivara/auth")
public class AuthController {

    private final OtpService otpService;
    private final UserRepository userRepository;

    private static final String CLIENT_ID = "356977800679-c2n68dfl1ovkta4v93t08dg76dkft1n5.apps.googleusercontent.com";

    public AuthController(OtpService otpService, UserRepository userRepository) {
        this.otpService = otpService;
        this.userRepository = userRepository;
    }

    // ✅ MUST MATCH token "aud"
    @PostMapping("/google")
    public User googleLogin(@RequestBody GoogleLoginRequest request) throws Exception {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .setIssuer("https://accounts.google.com") // ✅ important
                .build();

        GoogleIdToken idToken = verifier.verify(request.getIdToken());

        if (idToken == null) {
            throw new RuntimeException("Invalid Google token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User user = new User();
        user.setEmail(email);
        // user.setName(name);

        return userRepository.save(user);
    }

    @PostMapping("/otp/request")
    public ResponseEntity<?> requestOtp(@Valid @RequestBody MobileOtpRequestDto request) {
        otpService.sendMobileOtp(request.getMobileNumber());
        return ResponseEntity.ok(new ApiResponseDto(0, "OTP sent successfully"));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<AuthResponseDto> verifyOtp(
            @Valid @RequestBody MobileOtpVerifyDto request) {
        AuthResponseDto response = otpService.verifyMobileOtp(request.getMobileNumber(), request.getOtp());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/otp/email/request")
    public ResponseEntity<?> requestEmailOtp(@AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody EmailOtpRequestDto request) {
        Long userId = Long.valueOf(userDetails.getUsername());
        otpService.sendEmailOtp(userId, request.getEmail());
        return ResponseEntity.ok(new ApiResponseDto(0, "Email OTP sent"));
    }

    @PostMapping("/otp/email/verify")
    public ResponseEntity<?> verifyEmailOtp(@AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody EmailOtpVerifyDto request) {
        Long userId = Long.valueOf(userDetails.getUsername());
        otpService.verifyEmailOtp(userId, request.getEmail(), request.getOtp());
        return ResponseEntity.ok(new ApiResponseDto(0, "Email verified"));
    }

}
