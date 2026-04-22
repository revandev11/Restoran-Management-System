package com.ironhack.restoranmanagementsystem.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderCreateRequest {

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemAddRequest> items;

    public OrderCreateRequest() {}

    public List<OrderItemAddRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemAddRequest> items) {
        this.items = items;
    }
}