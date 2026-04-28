package com.ironhack.restoranmanagementsystem.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

public class ReservationUpdateRequest {
    @Future(message = "Reservation time must be in the future")
    @JsonProperty("reservation_time")
    private LocalDateTime reservationTime;

    @Min(value = 1, message = "Guest count must be at least 1")
    @JsonProperty("guest_count")
    private int guestCount;

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
