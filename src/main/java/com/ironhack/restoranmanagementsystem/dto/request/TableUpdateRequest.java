package com.ironhack.restoranmanagementsystem.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TableUpdateRequest {
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1 person")
    private int capacity;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}