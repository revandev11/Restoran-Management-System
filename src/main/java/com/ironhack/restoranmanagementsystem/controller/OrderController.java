package com.ironhack.restoranmanagementsystem.controller;

import com.ironhack.restoranmanagementsystem.dto.request.OrderCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.request.OrderItemAddRequest;
import com.ironhack.restoranmanagementsystem.dto.request.OrderUpdateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restoranmanagementsystem.dto.response.OrderSummary;
import com.ironhack.restoranmanagementsystem.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request,
                                                     @AuthenticationPrincipal String email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request, email));
    }

    @PostMapping("/{orderId}/items")
    public OrderResponse addItem(@PathVariable Long orderId,
                                 @Valid @RequestBody OrderItemAddRequest request,
                                 @AuthenticationPrincipal String email) {
        return orderService.addItem(orderId, request, email);
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable Long id,
                                 @AuthenticationPrincipal String email) {
        return orderService.getById(id, email, false);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderSummary> getAll() {
        return orderService.getAll();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateStatus(@PathVariable Long id,
                                      @Valid @RequestBody OrderUpdateRequest request) {
        return orderService.updateStatus(id, request.getStatus());
    }
}