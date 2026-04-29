package com.ironhack.restoranmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.restoranmanagementsystem.config.SecurityConfig;
import com.ironhack.restoranmanagementsystem.dto.request.OrderCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.request.OrderItemAddRequest;
import com.ironhack.restoranmanagementsystem.dto.response.OrderResponse;
import com.ironhack.restoranmanagementsystem.dto.response.OrderSummary;
import com.ironhack.restoranmanagementsystem.enums.OrderStatus;
import com.ironhack.restoranmanagementsystem.exception.CustomAccessDeniedHandler;
import com.ironhack.restoranmanagementsystem.exception.CustomAuthenticationEntryPoint;
import com.ironhack.restoranmanagementsystem.security.JwtTokenProvider;
import com.ironhack.restoranmanagementsystem.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private OrderResponse mockOrderResponse;

    @BeforeEach
    void setUp() {
        mockOrderResponse = new OrderResponse();
        mockOrderResponse.setId(1L);
        mockOrderResponse.setStatus(OrderStatus.PENDING);
        mockOrderResponse.setTotalPrice(BigDecimal.valueOf(20.00));
        mockOrderResponse.setCreatedAt(LocalDateTime.now());
        mockOrderResponse.setOrderItems(List.of());
    }

    @Test
    @WithMockUser(username = "test@mail.com", roles = "CUSTOMER")
    void shouldCreateOrderSuccessfully() throws Exception {
        OrderItemAddRequest item = new OrderItemAddRequest();
        item.setProductId(1L);
        item.setQuantity(2);

        OrderCreateRequest request = new OrderCreateRequest();
        request.setItems(List.of(item));

        when(orderService.createOrder(any(OrderCreateRequest.class), nullable(String.class)))
                .thenReturn(mockOrderResponse);

        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService).createOrder(any(OrderCreateRequest.class), nullable(String.class));
    }

    @Test
    void shouldReturnUnauthorizedWhenCreatingOrderWithoutLogin() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setItems(List.of());

        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(orderService);
    }

    @Test
    @WithMockUser(username = "test@mail.com", roles = "CUSTOMER")
    void shouldAddItemToOrderSuccessfully() throws Exception {
        OrderItemAddRequest item = new OrderItemAddRequest();
        item.setProductId(1L);
        item.setQuantity(1);

        when(orderService.addItem(eq(1L), any(OrderItemAddRequest.class), nullable(String.class)))
                .thenReturn(mockOrderResponse);

        mockMvc.perform(post("/api/orders/1/items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(orderService).addItem(eq(1L), any(OrderItemAddRequest.class), nullable(String.class));
    }

    @Test
    void shouldReturnUnauthorizedWhenAddingItemWithoutLogin() throws Exception {
        OrderItemAddRequest item = new OrderItemAddRequest();
        item.setProductId(1L);
        item.setQuantity(1);

        mockMvc.perform(post("/api/orders/1/items")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(orderService);
    }

    @Test
    @WithMockUser(username = "test@mail.com", roles = "CUSTOMER")
    void shouldReturnOrderByIdForOwner() throws Exception {
        when(orderService.getById(eq(1L), nullable(String.class), eq(false)))
                .thenReturn(mockOrderResponse);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.total_price").value(20.00));
    }

    @Test
    void shouldReturnUnauthorizedWhenGettingOrderWithoutLogin() throws Exception {
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(orderService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllOrdersForAdmin() throws Exception {
        OrderSummary summary = new OrderSummary(1L, LocalDateTime.now(),
                BigDecimal.valueOf(20.00), OrderStatus.PENDING, "Test User");

        when(orderService.getAll()).thenReturn(List.of(summary));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenWhenCustomerGetsAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateOrderStatusForAdmin() throws Exception {
        mockOrderResponse.setStatus(OrderStatus.PREPARING);
        when(orderService.updateStatus(eq(1L), eq(OrderStatus.PREPARING)))
                .thenReturn(mockOrderResponse);

        mockMvc.perform(patch("/api/orders/1/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"PREPARING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PREPARING"));

        verify(orderService).updateStatus(eq(1L), eq(OrderStatus.PREPARING));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenWhenCustomerUpdatesOrderStatus() throws Exception {
        mockMvc.perform(patch("/api/orders/1/status")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"PREPARING\"}"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(orderService);
    }
}

