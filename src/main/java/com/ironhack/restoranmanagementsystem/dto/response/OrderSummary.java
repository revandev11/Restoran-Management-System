package com.ironhack.restoranmanagementsystem.dto.response;

import com.ironhack.restoranmanagementsystem.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSummary {

    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private String userFullName;

    public OrderSummary() {}

    public OrderSummary(Long id, LocalDateTime createdAt, BigDecimal totalPrice,
                        OrderStatus status, String userFullName) {
        this.id = id;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.status = status;
        this.userFullName = userFullName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
}