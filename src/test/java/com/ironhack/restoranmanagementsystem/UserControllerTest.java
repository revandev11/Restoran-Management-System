package com.ironhack.restoranmanagementsystem;

import com.ironhack.restoranmanagementsystem.config.SecurityConfig;
import com.ironhack.restoranmanagementsystem.controller.UserController;
import com.ironhack.restoranmanagementsystem.dto.request.UserCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.dto.response.UserResponse;
import com.ironhack.restoranmanagementsystem.dto.response.UserSummary;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.OrderStatus;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import com.ironhack.restoranmanagementsystem.security.JwtTokenProvider;
import com.ironhack.restoranmanagementsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;


    @Test
    @WithMockUser(username = "test@mail.com")
    void shouldReturnCurrentUser() throws Exception {
        UserResponse response = new UserResponse(1L, "Test User", "test@mail.com", "123456789");

        when(userService.findByEmail(nullable(String.class))).thenReturn(response);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.fullName").value("Test User"));
    }

    @Test
    void shouldReturnUnauthorizedForMeEndpointWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(userService);
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void shouldReturnMyReservations() throws Exception {
        ReservationResponse r1 = new ReservationResponse();
        r1.setId(1L);
        r1.setReservationTime(LocalDateTime.of(2025, 1, 1, 19, 0));
        r1.setGuestCount(2);
        r1.setStatus(ReservationStatus.PENDING);

        ReservationResponse r2 = new ReservationResponse();
        r2.setId(2L);
        r2.setReservationTime(LocalDateTime.of(2025, 2, 14, 20, 0));
        r2.setGuestCount(4);
        r2.setStatus(ReservationStatus.PENDING);

        when(userService.getMyReservations(nullable(String.class))).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/users/me/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].guestCount").value(2))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].guestCount").value(4));
    }

    @Test
    void shouldReturnUnauthorizedForReservationsWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/users/me/reservations"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void shouldReturnMyOrders() throws Exception {
        OrderResponse o1 = new OrderResponse();
        o1.setId(1L);
        o1.setStatus(OrderStatus.PENDING);
        o1.setTotalPrice(BigDecimal.valueOf(45.00));

        OrderResponse o2 = new OrderResponse();
        o2.setId(2L);
        o2.setStatus(OrderStatus.COMPLETED);
        o2.setTotalPrice(BigDecimal.valueOf(30.00));

        when(userService.getMyOrders(nullable(String.class))).thenReturn(List.of(o1, o2));

        mockMvc.perform(get("/api/users/me/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void shouldReturnUnauthorizedForOrdersWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/users/me/orders"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllUsersForAdmin() throws Exception {
        List<UserSummary> users = List.of(
                new UserSummary(1L, "User One", "u1@mail.com"),
                new UserSummary(2L, "User Two", "u2@mail.com")
        );

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(userService).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenForGetAllUsersWhenNotAdmin() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteUserForAdmin() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenForDeleteWhenNotAdmin() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateUserForAdmin() throws Exception {
        User savedUser = new User();
        savedUser.setId(10L);
        savedUser.setFullName("New User");
        savedUser.setEmail("new@mail.com");
        savedUser.setPhoneNumber("987654321");

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(savedUser);

        String requestBody = """
                {
                    "fullName": "New User",
                    "email": "new@mail.com",
                    "phoneNumber": "987654321",
                    "password": "secret"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@mail.com"))
                .andExpect(jsonPath("$.fullName").value("New User"));

        verify(userService).createUser(any(UserCreateRequest.class));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenForCreateWhenNotAdmin() throws Exception {
        String requestBody = """
                {
                    "fullName": "New User",
                    "email": "new@mail.com",
                    "phoneNumber": "987654321",
                    "password": "secret"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());

        verifyNoInteractions(userService);
    }

    @Test
    void shouldReturnUnauthorizedForCreateWhenNotAuthenticated() throws Exception {
        String requestBody = """
                {
                    "fullName": "New User",
                    "email": "new@mail.com",
                    "phoneNumber": "987654321",
                    "password": "secret"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is4xxClientError());
    }
}