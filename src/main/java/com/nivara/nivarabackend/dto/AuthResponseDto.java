package com.nivara.nivarabackend.dto;

public class AuthResponseDto {

    private String token;
    private boolean emailVerified;
    private boolean profileCompleted;

    public AuthResponseDto(String token, boolean emailVerified, boolean profileCompleted) {
        this.token = token;
        this.emailVerified = emailVerified;
        this.profileCompleted = profileCompleted;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(boolean profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    
}
