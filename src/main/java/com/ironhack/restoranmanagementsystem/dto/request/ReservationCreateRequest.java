package com.ironhack.restoranmanagementsystem.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ReservationCreateRequest {
    @NotNull(message = "Reservation time is required")
    @Future(message = "Reservation time must be in the future")
    @JsonProperty("reservation_time")
    private LocalDateTime reservationTime;

    @NotNull(message = "Guest count is required")
    @Min(value = 1, message = "Guest count must be at least 1")
    @JsonProperty("guest_count")
    private int guestCount;

    @NotNull(message = "Table ID is required")
    @JsonProperty("restaurant_table_id")
    private Long restaurantTableId;

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

    public Long getRestaurantTableId() {
        return restaurantTableId;
    }

    public void setRestaurantTableId(Long restaurantTableId) {
        this.restaurantTableId = restaurantTableId;
    }
}
