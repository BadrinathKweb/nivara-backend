package com.nivara.nivarabackend.repository;

import com.nivara.nivarabackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhone(String phoneNumber);
    Optional<User> findByEmail(String email);
}
