package com.ironhack.restoranmanagementsystem.controller;

import com.ironhack.restoranmanagementsystem.dto.request.UserRequest;
import com.ironhack.restoranmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.dto.response.UserResponse;
import com.ironhack.restoranmanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/me/reservations")
    public List<ReservationResponse> getMyReservations(@AuthenticationPrincipal String email) {
        return userService.getMyReservations(email);
    }

    @GetMapping("/me/orders")
    public List<OrderResponse> getMyOrders(@AuthenticationPrincipal String email) {
        return userService.getMyOrders(email);
    }
}
