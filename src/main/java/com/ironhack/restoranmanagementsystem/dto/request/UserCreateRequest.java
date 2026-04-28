package com.ironhack.restoranmanagementsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ironhack.restoranmanagementsystem.enums.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.NumberFormat;

public class UserCreateRequest {
    @NotBlank(message = "Full name is required!")
    @Size(min = 6, max=100, message = "Full name must be 2-200 characters")
    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email format is not correct!")
    @Size(min = 6, max=100, message = "Email must be 2-200 characters!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Size(min=6, message = "Password must have at least 6 characters!")
    private String password;

    @NotBlank(message = "Phone number is required!")
    @NumberFormat
    @JsonProperty("phone_number")
    private String phoneNumber;

    private String role;

    public UserCreateRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
