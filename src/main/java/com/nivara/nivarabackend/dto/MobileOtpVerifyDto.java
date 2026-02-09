package com.nivara.nivarabackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class MobileOtpVerifyDto {

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits")
    private String mobileNumber;

    @NotBlank
    @Pattern(regexp = "\\d{4,6}", message = "OTP must be 4-6 digits")
    private String otp;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
    
}
