package com.ironhack.restoranmanagementsystem.dto.response;

public class TableResponse {
    private Long id;
    private int tableNumber;
    private int capacity;
    private Boolean isAvailable;
    public TableResponse(){}

    public TableResponse(Long id, int tableNumber, int capacity, Boolean isAvailable) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
