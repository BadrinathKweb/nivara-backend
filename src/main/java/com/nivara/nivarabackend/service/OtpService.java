package com.nivara.nivarabackend.service;

import com.nivara.nivarabackend.dto.*;

public interface OtpService {

    void sendMobileOtp(String mobile);

    AuthResponseDto verifyMobileOtp(String mobile, String otp);

    void sendEmailOtp(Long userId, String email);

    void verifyEmailOtp(Long userId, String email, String otp);

}
