package com.ironhack.restoranmanagementsystem.service;

import com.ironhack.restoranmanagementsystem.dto.request.RegisterRequest;
import com.ironhack.restoranmanagementsystem.dto.request.UserRequest;
import com.ironhack.restoranmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.dto.response.UserResponse;
import com.ironhack.restoranmanagementsystem.entity.Order;
import com.ironhack.restoranmanagementsystem.entity.Reservation;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.RoleName;
import com.ironhack.restoranmanagementsystem.mapper.OrderMapper;
import com.ironhack.restoranmanagementsystem.mapper.ReservationMapper;
import com.ironhack.restoranmanagementsystem.mapper.UserMapper;
import com.ironhack.restoranmanagementsystem.repository.OrderRepository;
import com.ironhack.restoranmanagementsystem.repository.ReservationRepository;
import com.ironhack.restoranmanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReservationRepository reservationRepository;
    private final OrderRepository orderRepository;
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ReservationRepository reservationRepository,
                       OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.reservationRepository = reservationRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "User with email " + request.getEmail() + " already exists"
            );
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        RoleName role = RoleName.CUSTOMER;
        if (request.getRole() != null) {
            role = RoleName.valueOf(request.getRole().toUpperCase());
        }

        user.setRole(role);

        return userRepository.save(user);
    }

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + email));

        return UserMapper.toResponse(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + email));
    }

    public List<ReservationResponse> getMyReservations(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Reservation> reservations = reservationRepository.findByUser(user);

        return ReservationMapper.toResponseList(reservations);
    }

    public List<OrderResponse> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUser(user);

        return OrderMapper.toResponseList(orders);
    }
}
