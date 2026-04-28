package com.ironhack.restoranmanagementsystem.service;

import com.ironhack.restoranmanagementsystem.dto.request.ReservationCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.request.ReservationUpdateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.entity.Reservation;
import com.ironhack.restoranmanagementsystem.entity.RestaurantTable;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import com.ironhack.restoranmanagementsystem.exception.BadRequestException;
import com.ironhack.restoranmanagementsystem.exception.ConflictException;
import com.ironhack.restoranmanagementsystem.exception.ForbiddenException;
import com.ironhack.restoranmanagementsystem.exception.ResourceNotFoundException;
import com.ironhack.restoranmanagementsystem.mapper.ReservationMapper;
import com.ironhack.restoranmanagementsystem.repository.ReservationRepository;
import com.ironhack.restoranmanagementsystem.repository.RestaurantTableRepository;
import com.ironhack.restoranmanagementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, RestaurantTableService restaurantTableService, RestaurantTableRepository restaurantTableRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    @Transactional
    public ReservationResponse createReservation(String email, ReservationCreateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));

        RestaurantTable table = restaurantTableRepository.findById(request.getRestaurantTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + request.getRestaurantTableId()));

        if (!table.isAvailable()) {
            throw new BadRequestException("Table is not available");
        }

        if (request.getGuestCount() > table.getCapacity()) {
            throw new BadRequestException("Guest count cannot be greater than table capacity");
        }

        boolean conflictExists = reservationRepository.existsByRestaurantTableIdAndReservationTimeAndStatusNot(
                request.getRestaurantTableId(),
                request.getReservationTime(),
                ReservationStatus.CANCELLED
        );

        if (conflictExists) {
            throw new ConflictException("This table is already reserved for the selected time");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRestaurantTable(table);
        reservation.setReservationTime(request.getReservationTime());
        reservation.setGuestCount(request.getGuestCount());
        reservation.setStatus(ReservationStatus.PENDING);

        return ReservationMapper.toResponse(reservationRepository.save(reservation));
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ReservationMapper.toResponseList(reservations);
    }

    public ReservationResponse getById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        return ReservationMapper.toResponse(reservation);
    }

    public List<ReservationResponse> getMyReservations(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Reservation> reservations = reservationRepository.findByUser(user);
        return ReservationMapper.toResponseList(reservations);
    }

    public ReservationResponse confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return ReservationMapper.toResponse(reservationRepository.save(reservation));
    }

    @Transactional
    public ReservationResponse cancelReservation(Long id, String email, boolean isAdmin) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        if (!isAdmin && !reservation.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("You can only cancel your own reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return ReservationMapper.toResponse(reservationRepository.save(reservation));
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found with id: " + id);
        }
        reservationRepository.deleteById(id);
    }

    @Transactional
    public ReservationResponse updateReservation(Long id, ReservationUpdateRequest request, String email, boolean isAdmin) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));

        if (!isAdmin && !reservation.getUser().getEmail().equals(email)) {
            throw new ForbiddenException("You can only update your own reservation");
        }

        RestaurantTable table = reservation.getRestaurantTable();
        Long tableId = table.getId();

        if (request.getRestaurantTableId() != null) {
            table = restaurantTableRepository.findById(request.getRestaurantTableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + request.getRestaurantTableId()));

            reservation.setRestaurantTable(table);
            tableId = request.getRestaurantTableId();
        }

        if (request.getReservationTime() != null) {
            reservation.setReservationTime(request.getReservationTime());
        }

        if (request.getGuestCount() != null) {
            reservation.setGuestCount(request.getGuestCount());
        }

        if (!table.isAvailable()) {
            throw new BadRequestException("Table is not available");
        }

        if (reservation.getGuestCount() > table.getCapacity()) {
            throw new BadRequestException("Guest count cannot be greater than table capacity");
        }

        boolean conflictExists = reservationRepository.existsByRestaurantTableIdAndReservationTimeAndStatusNotAndIdNot(
                tableId,
                reservation.getReservationTime(),
                ReservationStatus.CANCELLED,
                reservation.getId()
        );

        if (conflictExists) {
            throw new ConflictException("This table is already reserved for the selected time");
        }

        reservation.setStatus(ReservationStatus.PENDING);

        return ReservationMapper.toResponse(reservationRepository.save(reservation));
    }

    public List<ReservationResponse> getReservationByStatus(ReservationStatus status) {
        List<Reservation> reservations = reservationRepository.findByStatus(status);
        return ReservationMapper.toResponseList(reservations);
    }

    public List<ReservationResponse> getByUserIdOrderByTime(Long userId) {
        List<Reservation> reservations = reservationRepository.findByUserIdOrderByReservationTimeDesc(userId);
        return ReservationMapper.toResponseList(reservations);
    }

    public List<ReservationResponse> getByMinGuestCount(int count) {
        List<Reservation> reservations = reservationRepository.findByGuestCountGreaterThanEqual(count);
        return ReservationMapper.toResponseList(reservations);
    }
}
