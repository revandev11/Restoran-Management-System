package com.ironhack.restoranmanagementsystem.dto.response;

import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationResponse {
    private Long id;

    @NotNull(message = "Reservation time is required")
    @Future(message = "Reservation time must be in the future")
    private LocalDateTime reservationTime;

    @Min(value = 1, message = "Guest count must be at least 1")
    @Max(value = 20, message = "Guest count cannot exceed 20 people per table")
    private int guestCount;

    @NotNull(message = "Reservation status is required")
    private ReservationStatus status;
    @PastOrPresent(message = "Creation date cannot be in the future")
    private LocalDate createdAt;
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotBlank(message = "User full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    private String userFullName;
    @NotNull(message = "Table ID is required")
    private Long tableId;
    @Positive(message = "Table number must be a positive value")
    private int tableNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }
}
