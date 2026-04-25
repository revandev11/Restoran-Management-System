package com.ironhack.restoranmanagementsystem.service;
import com.ironhack.restoranmanagementsystem.dto.request.ReservationCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.request.ReservationUpdateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.entity.Reservation;
import com.ironhack.restoranmanagementsystem.entity.RestaurantTable;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import com.ironhack.restoranmanagementsystem.mapper.ReservationMapper;
import com.ironhack.restoranmanagementsystem.repository.ReservationRepository;
import com.ironhack.restoranmanagementsystem.repository.RestaurantTableRepository;
import com.ironhack.restoranmanagementsystem.repository.UserRepository;
import jakarta.persistence.Table;
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
    public ReservationResponse createReservation(Long userId, ReservationCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        RestaurantTable table =restaurantTableRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + request.getRestaurantTableId()));
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setRestaurantTable(table);
        reservation.setReservationTime(request.getReservationTime());
        reservation.setGuestCount(request.getGuestCount());
        reservation.setStatus(ReservationStatus.PENDING);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationMapper.toResponse(savedReservation);
    }
    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ReservationMapper.toResponseList(reservations);
    }
    public ReservationResponse getById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        return ReservationMapper.toResponse(reservation);
    }

    public List<ReservationResponse> getMyReservations(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Reservation> reservations = reservationRepository.findByUser(user);
        return ReservationMapper.toResponseList(reservations);
    }

    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Reservation not found with id: " + id);
        }
        reservationRepository.deleteById(id);
    }

    public ReservationResponse updateReservation(Long id, ReservationUpdateRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id:" + id));
        reservation.setReservationTime(request.getReservationTime());
        reservation.setGuestCount(request.getGuestCount());
        Reservation updatedReservation = reservationRepository.save(reservation);
        return ReservationMapper.toResponse(updatedReservation);
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
