package com.ironhack.restoranmanagementsystem.dto.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TableCreateRequest {
    @NotNull(message = "Table number is required")
    @Positive(message = "Table number must be a positive value")
    @JsonProperty("table_number")
    private int tableNumber;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1 person")
    private int capacity;

    @NotNull(message = "Availability status is required")
    @JsonProperty("is_available")
    private Boolean isAvailable;
    public TableCreateRequest(){}

    public TableCreateRequest(int tableNumber, int capacity, Boolean isAvailable) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }


    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

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
