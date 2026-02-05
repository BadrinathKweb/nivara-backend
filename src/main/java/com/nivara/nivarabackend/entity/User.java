package com.nivara.nivarabackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    public User() {
    }

    public User(String name, String email,String phone) {
        this.name = name;
        this.email = email;
        this.phone=phone;
    }
    
    // getters & setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getphoneNumber() {
        return phone;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
     public void setEmail(String email) {
        this.email = email;
    }
     public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
}
