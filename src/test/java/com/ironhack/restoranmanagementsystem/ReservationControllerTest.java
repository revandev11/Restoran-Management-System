package com.ironhack.restoranmanagementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.restoranmanagementsystem.config.SecurityConfig;
import com.ironhack.restoranmanagementsystem.controller.ReservationController;
import com.ironhack.restoranmanagementsystem.dto.request.ReservationCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import com.ironhack.restoranmanagementsystem.exception.CustomAccessDeniedHandler;
import com.ironhack.restoranmanagementsystem.exception.CustomAuthenticationEntryPoint;
import com.ironhack.restoranmanagementsystem.security.JwtTokenProvider;
import com.ironhack.restoranmanagementsystem.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private ReservationResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = new ReservationResponse();
        mockResponse.setId(1L);
        mockResponse.setGuestCount(4);
        mockResponse.setStatus(ReservationStatus.PENDING);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER", username = "test@example.com")
    void createReservation_Success() throws Exception {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setReservationTime(LocalDateTime.now().plusHours(2));
        request.setGuestCount(4);
        request.setRestaurantTableId(10L);

        when(reservationService.createReservation(any(), any()))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.guest_count").value(4))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @WithMockUser
    void getById_Success() throws Exception {
        when(reservationService.getById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void confirmReservation_Success() throws Exception {
        mockResponse.setStatus(ReservationStatus.CONFIRMED);
        when(reservationService.confirmReservation(1L)).thenReturn(mockResponse);

        mockMvc.perform(patch("/api/reservations/1/confirm")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void confirmReservation_ForbiddenForUser() throws Exception {
        mockMvc.perform(patch("/api/reservations/1/confirm")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteReservation_Success() throws Exception {
        mockMvc.perform(delete("/api/reservations/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void getByStatus_Success() throws Exception {
        when(reservationService.getReservationByStatus(ReservationStatus.PENDING))
                .thenReturn(Collections.singletonList(mockResponse));

        mockMvc.perform(get("/api/reservations/status")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}