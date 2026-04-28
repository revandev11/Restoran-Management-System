package com.ironhack.restoranmanagementsystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.restoranmanagementsystem.config.SecurityConfig;
import com.ironhack.restoranmanagementsystem.controller.ReservationController;
import com.ironhack.restoranmanagementsystem.controller.RestaurantTableController;
import com.ironhack.restoranmanagementsystem.dto.request.ReservationCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.ReservationResponse;
import com.ironhack.restoranmanagementsystem.enums.ReservationStatus;
import com.ironhack.restoranmanagementsystem.security.JwtTokenProvider;
import com.ironhack.restoranmanagementsystem.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class, JacksonAutoConfiguration.class})
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    ObjectMapper objectMapper=new ObjectMapper()
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
    @WithMockUser
    void createReservation_Success() throws Exception {
        ReservationCreateRequest request=new ReservationCreateRequest();
        request.setReservationTime(LocalDateTime.now().plusHours(2));  // gələcək vaxt
        request.setGuestCount(4);
        request.setRestaurantTableId(10L);
        when(reservationService.createReservation(eq(1L), any(ReservationCreateRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/reservation/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    @WithMockUser
    void getById_Success() throws Exception {
        when(reservationService.getById(1L)).thenReturn(mockResponse);
        mockMvc.perform(get("/api/reservation/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void confirmReservation_Success() throws Exception {
        mockResponse.setStatus(ReservationStatus.CONFIRMED);
        when(reservationService.confirmReservation(1L)).thenReturn(mockResponse);

        mockMvc.perform(patch("/api/reservation/1/confirm")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }
    @Test
    @WithMockUser(roles="USER")
    void confirmReservation_ForbiddenForUser() throws Exception {
        mockMvc.perform(patch("/api/reservation/1/confirm")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles="ADMIN")
    void deleteReservation_Success() throws Exception {
        mockMvc.perform(delete("/api/reservation/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    void getByStatus_Success() throws Exception {
        when(reservationService.getReservationByStatus(ReservationStatus.PENDING))
                .thenReturn(Collections.singletonList(mockResponse));
        mockMvc.perform(get("/api/reservation/status")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}