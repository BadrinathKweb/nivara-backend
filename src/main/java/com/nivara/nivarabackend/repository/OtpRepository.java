package com.nivara.nivarabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nivara.nivarabackend.entity.Otp;
import com.nivara.nivarabackend.entity.OtpType;
import com.nivara.nivarabackend.entity.OtpVerification;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findTopByIdentifierAndTypeAndVerifiedFalseOrderByIdDesc(
            String identifier, OtpType type);
}
