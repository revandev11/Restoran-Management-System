package com.ironhack.restoranmanagementsystem.entity;

import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import jakarta.persistence.*;

import com.ironhack.restoranmanagementsystem.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "reservation_time")
    private LocalDateTime reservationTime;

    @Column(nullable = false, name = "guest_count")
    private  int guestCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false,updatable = false)
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private RestaurantTable restaurantTable;

    public Reservation(){}

    public Reservation(LocalDateTime reservationTime, int guestCount, ReservationStatus status, User user, LocalDate createdAt, RestaurantTable restaurantTable) {
        this.reservationTime = reservationTime;
        this.guestCount = guestCount;
        this.status = status;
        this.user = user;
        this.createdAt = createdAt;
        this.restaurantTable = restaurantTable;
    }



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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }
}
