package com.nivara.nivarabackend.dto;

public class AuthResponseDto {

    private String token;
    private String mobileNumber;

    public AuthResponseDto(String token, String mobileNumber) {
        this.token = token;
        this.mobileNumber = mobileNumber;
    }

    public String getToken() {
        return token;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
