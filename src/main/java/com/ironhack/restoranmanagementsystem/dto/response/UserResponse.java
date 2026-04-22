package com.ironhack.restoranmanagementsystem.dto.response;

public class UserResponse {
    private final Long id;
    private final String fullName;
    private final String email;
    private final String phoneNumber;

    public UserResponse(Long id, String fullName, String email, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}