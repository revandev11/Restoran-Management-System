package com.ironhack.restoranmanagementsystem.service;

import com.ironhack.restoranmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.dto.response.UserResponse;
import com.ironhack.restoranmanagementsystem.entity.Order;
import com.ironhack.restoranmanagementsystem.entity.Reservation;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.mapper.OrderMapper;
import com.ironhack.restoranmanagementsystem.mapper.ReservationMapper;
import com.ironhack.restoranmanagementsystem.mapper.UserMapper;
import com.ironhack.restoranmanagementsystem.repository.OrderRepository;
import com.ironhack.restoranmanagementsystem.repository.ReservationRepository;
import com.ironhack.restoranmanagementsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;
    private final OrderRepository orderRepository;
    public UserService(UserRepository userRepository,
                       ReservationRepository reservationRepository,
                       OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.orderRepository = orderRepository;
    }



    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + email));

        return UserMapper.toResponse(user);
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
