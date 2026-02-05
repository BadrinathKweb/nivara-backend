package com.nivara.nivarabackend.service;

import com.nivara.nivarabackend.dto.*;

public interface OtpService {

    void sendOtp(OtpRequestDto request);

    AuthResponseDto verifyOtp(OtpVerifyDto request);
}
