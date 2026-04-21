package com.ironhack.restoranmanagementsystem.repository;

import com.ironhack.restoranmanagementsystem.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantTable,Long> {
    Optional<RestaurantTable> findByTableNumber(int tableNumber);
    List<RestaurantTable> findAllByAvailable(Boolean available);
    List<RestaurantTable> findByCapacityGreaterThanEqual(int capacity);
}
