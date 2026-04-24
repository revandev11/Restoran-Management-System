package com.ironhack.restoranmanagementsystem;

import com.ironhack.restoranmanagementsystem.dto.request.UserCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.dto.response.UserSummary;
import com.ironhack.restoranmanagementsystem.entity.Order;
import com.ironhack.restoranmanagementsystem.entity.Reservation;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.RoleName;
import com.ironhack.restoranmanagementsystem.exception.ConflictException;
import com.ironhack.restoranmanagementsystem.repository.OrderRepository;
import com.ironhack.restoranmanagementsystem.repository.ReservationRepository;
import com.ironhack.restoranmanagementsystem.repository.UserRepository;
import com.ironhack.restoranmanagementsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("test@mail.com");
        request.setFullName("Test User");
        request.setPhoneNumber("123456");
        request.setPassword("1234");
        request.setRole("customer");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("encoded_pass");

        User savedUser = new User();
        savedUser.setEmail("test@mail.com");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("1234");
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("test@mail.com");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(ConflictException.class, () ->
                userService.createUser(request)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldFindUserByEmail() {
        User user = new User();
        user.setEmail("test@mail.com");

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        var response = userService.findByEmail("test@mail.com");

        assertNotNull(response);
        assertEquals("test@mail.com", response.getEmail());
    }

    @Test
    void shouldThrowWhenUserNotFoundByEmail() {
        when(userRepository.findByEmail("notfound@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                userService.findByEmail("notfound@mail.com")
        );
    }

    @Test
    void shouldDeleteUser() {
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldSetDefaultRoleWhenNull() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("test@mail.com");
        request.setPassword("1234");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(request);

        assertEquals(RoleName.CUSTOMER, result.getRole());
    }

    @Test
    void shouldReturnMyReservations() {
        User user = new User();
        user.setEmail("test@mail.com");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setGuestCount(2);

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(reservationRepository.findByUser(user)).thenReturn(List.of(reservation));

        List<ReservationResponse> result = userService.getMyReservations("test@mail.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository).findByUser(user);
    }

    @Test
    void shouldThrowWhenUserNotFoundForReservations() {
        when(userRepository.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                userService.getMyReservations("notfound@mail.com")
        );
    }

    @Test
    void shouldReturnMyOrders() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setFullName("Test User");

        Order order = new Order();
        order.setId(1L);
        order.setTotalPrice(BigDecimal.valueOf(50.00));
        order.setUser(user);

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(List.of(order));

        List<OrderResponse> result = userService.getMyOrders("test@mail.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository).findByUser(user);
    }

    @Test
    void shouldThrowWhenUserNotFoundForOrders() {
        when(userRepository.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                userService.getMyOrders("notfound@mail.com")
        );
    }

    @Test
    void shouldReturnAllUsers() {
        User u1 = new User();
        u1.setId(1L);
        u1.setEmail("u1@mail.com");

        User u2 = new User();
        u2.setId(2L);
        u2.setEmail("u2@mail.com");

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UserSummary> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserSummary> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}