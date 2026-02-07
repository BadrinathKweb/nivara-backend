package com.nivara.nivarabackend.controller;

import com.nivara.nivarabackend.entity.User;
import com.nivara.nivarabackend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/nivara/users")
public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // ✅ Get user by phone number
    @GetMapping("/by-phone/{phone}")
    public Optional<User> getByPhone(@PathVariable String phone) {
        return userRepository.findByPhone(phone); // match entity field name
    }

    // ✅ Create user
    @PostMapping
    public User createUser(@RequestBody User user) {
        System.out.println("EMAIL = " + user.getEmail());
        return userRepository.save(user);
    }
}
