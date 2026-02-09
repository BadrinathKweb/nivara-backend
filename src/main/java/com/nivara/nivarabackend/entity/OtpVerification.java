package com.nivara.nivarabackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "otp_verifications",
        indexes = {
                @Index(name = "idx_identifier", columnList = "identifier"),
                @Index(name = "idx_expires", columnList = "expiresAt")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // mobile or email
    @Column(nullable = false)
    private String identifier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpType type; // MOBILE / EMAIL

    @Column(nullable = false, length = 6)
    private String otpCode;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder.Default
    private boolean verified = false;

    @Column(updatable = false)
    private LocalDateTime createdAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
