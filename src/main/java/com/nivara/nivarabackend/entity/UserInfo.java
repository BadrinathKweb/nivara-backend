package com.nivara.nivarabackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 FK mapping
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


    // ---------- Profile ----------
    private String firstName;
    private String middleName;
    private String lastName;

    private boolean driver;

    private String govtIdType;
    private String govtIdNumber;

    private String vehicleType;
    private String vehicleNumber;
}
