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
import static org.mockito.Mockito.*;


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
    void setUp(){
        user = new User();
        user.setId(1L);

        table=new RestaurantTable();
        table.setId(10L);

        reservation=new Reservation();
        reservation.setId(100L);
        reservation.setUser(user);
        reservation.setRestaurantTable(table);
        reservation.setStatus(ReservationStatus.PENDING);
    }
    @Test
    void createReservation_Success(){
        ReservationCreateRequest request=new ReservationCreateRequest();
        request.setRestaurantTableId(10L);
        request.setGuestCount(4);
        request.setReservationTime(LocalDateTime.now().plusDays(1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(restaurantTableRepository.findById(any())).thenReturn(Optional.of(table));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        ReservationResponse response=reservationService.createReservation(1L, request);
        assertNotNull(response);
        verify(reservationRepository,times(1)).save(any(Reservation.class));
    }
    @Test
    void createReservation_UserNotFound_ThrowsException(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,() ->
                reservationService.createReservation(1L,new ReservationCreateRequest())
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
    void cancelReservation_Success(){
        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        ReservationResponse response=reservationService.cancelReservation(100L);
        assertEquals(ReservationStatus.CANCELLED,reservation.getStatus());
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
