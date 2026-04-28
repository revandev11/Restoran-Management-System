package com.ironhack.restoranmanagementsystem.controller;

import com.ironhack.restoranmanagementsystem.dto.request.UserCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.request.UserUpdateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.dto.response.UserResponse;
import com.ironhack.restoranmanagementsystem.dto.response.UserSummary;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserSummary> getAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request){
        User user =  userService.createUser(request);
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request){
        User user =  userService.updateUser(id, request);
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}
