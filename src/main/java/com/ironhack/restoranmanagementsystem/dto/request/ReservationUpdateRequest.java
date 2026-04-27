package com.ironhack.restoranmanagementsystem.dto.request;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

public class ReservationUpdateRequest {
    @Future(message = "Reservation time must be in the future")
    private LocalDateTime reservationTime;
    @Min(value = 1, message = "Guest count must be at least 1")
    private int guestCount;
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

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public Long getRestaurantTableId() {
        return restaurantTableId;
    }

    public void setRestaurantTableId(Long restaurantTableId) {
        this.restaurantTableId = restaurantTableId;
    }

}
