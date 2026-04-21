package com.ironhack.restoranmanagementsystem.repository;

import com.ironhack.restoranmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFullName(String fullName);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
