package com.ironhack.restoranmanagementsystem;

import com.ironhack.restoranmanagementsystem.dto.request.ReservationCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.entity.Reservation;
import com.ironhack.restoranmanagementsystem.entity.RestaurantTable;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import com.ironhack.restoranmanagementsystem.repository.ReservationRepository;
import com.ironhack.restoranmanagementsystem.repository.RestaurantTableRepository;
import com.ironhack.restoranmanagementsystem.repository.UserRepository;
import com.ironhack.restoranmanagementsystem.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantTableRepository restaurantTableRepository;
    @InjectMocks
    private ReservationService reservationService;
    private User user;
    private RestaurantTable table;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");

        table = new RestaurantTable();
        table.setId(10L);
        table.setCapacity(4);
        table.setAvailable(true);

        reservation = new Reservation();
        reservation.setId(100L);
        reservation.setUser(user);
        reservation.setRestaurantTable(table);
        reservation.setStatus(ReservationStatus.PENDING);
    }
    @Test
    void createReservation_Success() {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setRestaurantTableId(10L);
        request.setGuestCount(4);
        request.setReservationTime(LocalDateTime.now().plusDays(1));
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(restaurantTableRepository.findById(10L)).thenReturn(Optional.of(table));
        when(reservationRepository.existsByRestaurantTableIdAndReservationTimeAndStatusNot(any(), any(), any())).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        ReservationResponse response = reservationService.createReservation("test@mail.com", request);
        assertNotNull(response);
        verify(reservationRepository).save(any(Reservation.class));
    }
    @Test
    void createReservation_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        assertThrows(Exception.class, () ->
                reservationService.createReservation("test@mail.com", new ReservationCreateRequest())
        );
    }
    @Test
    void getById_Success(){
        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));
        ReservationResponse response=reservationService.getById(100L);
        assertNotNull(response);
        assertEquals(ReservationStatus.PENDING.name(),response.getStatus().name());
    }
    @Test
    void cancelReservation_Success() {
        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any())).thenReturn(reservation);
        ReservationResponse response = reservationService.cancelReservation(100L, "test@mail.com", false);
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        verify(reservationRepository).save(reservation);
    }
    @Test
    void deleteReservation_NotFound_ThrowsException(){
        when(reservationRepository.existsById(100L)).thenReturn(false);
        RuntimeException exception=assertThrows(RuntimeException.class, () ->
                reservationService.deleteReservation(100L)
        );
        assertTrue(exception.getMessage().contains("Reservation not found"));
    }
}
