package com.nivara.nivarabackend.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.nivara.nivarabackend.dto.AuthResponseDto;
import com.nivara.nivarabackend.dto.GoogleLoginRequest;
import com.nivara.nivarabackend.dto.OtpRequestDto;
import com.nivara.nivarabackend.dto.OtpVerifyDto;
import com.nivara.nivarabackend.entity.User;
import com.nivara.nivarabackend.repository.UserRepository;
import com.nivara.nivarabackend.service.OtpService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/nivara/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final OtpService otpService;

    // ✅ MUST MATCH token "aud"
    private static final String CLIENT_ID =
        "356977800679-c2n68dfl1ovkta4v93t08dg76dkft1n5.apps.googleusercontent.com";
         public AuthController(UserRepository userRepository, OtpService otpService) {
    this.userRepository = userRepository;
    this.otpService = otpService;
}


    @PostMapping("/google")
    public User googleLogin(@RequestBody GoogleLoginRequest request) throws Exception {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance()
        )
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
        user.setName(name);

        return userRepository.save(user);
    }


    @PostMapping("/otp/request")
public ResponseEntity<?> requestOtp(@RequestBody OtpRequestDto request) {
    try {
        System.out.println("Received OTP request: " + request); // Add this
        otpService.sendOtp(request);
        return ResponseEntity.ok("OTP sent successfully");
    } catch (Exception ex) {
        ex.printStackTrace(); // Add this to see full stack trace
        return ResponseEntity.badRequest().body("Failed to send OTP: " + ex.getMessage());
    }
}

    @PostMapping("/otp/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerifyDto request) {
        try {
            AuthResponseDto response = otpService.verifyOtp(request);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(request.getMobileNumber(), ex.getMessage())
            );
        }
    }

    
    private static class ErrorResponse {
        private String mobileNumber;
        private String error;

        public ErrorResponse(String mobileNumber, String error) {
            this.mobileNumber = mobileNumber;
            this.error = error;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public String getError() {
            return error;
        }
    }
}
