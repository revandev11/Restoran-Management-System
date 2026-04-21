package com.ironhack.restoranmanagementsystem.repository;

import com.ironhack.restoranmanagementsystem.entity.Order;
import com.ironhack.restoranmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByUser(User user);
}