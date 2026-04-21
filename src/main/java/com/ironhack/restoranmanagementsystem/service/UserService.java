package com.ironhack.restoranmanagementsystem.service;

import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.RoleName;
import com.ironhack.restoranmanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String email, String rawPassword, String fullName) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("A user with email " + email + " already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(RoleName.CUSTOMER);

        return userRepository.save(user);
    }

    @Transactional
    public User create(String email, String rawPassword, String fullName, RoleName role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("A user with email " + email + " already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role != null ? role : RoleName.CUSTOMER);

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
