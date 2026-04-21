package com.ironhack.restoranmanagementsystem.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "restaurant_table")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_number", unique = true, nullable = false)
    private int tableNumber;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false, name = "is_available")
    private boolean isAvailable;

    @OneToMany(mappedBy ="restaurantTable", cascade = CascadeType.ALL)
    private List<Reservation>reservations;

    public RestaurantTable(){}

    public RestaurantTable(int tableNumber, int capacity, boolean isAvailable, List<Reservation> reservations) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
        this.reservations = reservations;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
