package com.ironhack.restoranmanagementsystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoding() {
        String rawPassword = "password123";
        String encoded = passwordEncoder.encode(rawPassword);

        assertTrue(encoded.startsWith("$2a$"));

        String encoded2 = passwordEncoder.encode(rawPassword);
        assertNotEquals(encoded, encoded2);

        assertTrue(passwordEncoder.matches(rawPassword, encoded));
        assertTrue(passwordEncoder.matches(rawPassword, encoded2));

        assertFalse(passwordEncoder.matches("wrongpassword", encoded));
    }
}
