package com.nivara.nivarabackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class MobileOtpRequestDto {

    @NotBlank(message = "Mobile number required")
    @Pattern(regexp = "\\d{10}", message = "Mobile must be 10 digits")
    private String mobileNumber;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
