package com.ironhack.restoranmanagementsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequest {
    @NotBlank(message = "Email is required!")
    @Email(message = "Email format is not correct!")
    @Size(min = 6, max=100, message = "Email must be 2-200 characters!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min=6, message = "Password must have at least 6 characters!")
    private String password;

    public AuthRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}