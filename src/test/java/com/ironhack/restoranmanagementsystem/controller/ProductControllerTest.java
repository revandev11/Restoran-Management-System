package com.ironhack.restoranmanagementsystem.controller;

import com.ironhack.restoranmanagementsystem.config.SecurityConfig;
import com.ironhack.restoranmanagementsystem.dto.response.ProductResponse;
import com.ironhack.restoranmanagementsystem.exception.CustomAccessDeniedHandler;
import com.ironhack.restoranmanagementsystem.exception.CustomAuthenticationEntryPoint;
import com.ironhack.restoranmanagementsystem.security.JwtTokenProvider;
import com.ironhack.restoranmanagementsystem.service.ProductService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;


    private ProductResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = new ProductResponse(1L, "Burger", "Tasty burger",
                new BigDecimal("8.50"), true, 1L, "Burgers");
    }

    @Test
    @WithMockUser
    void shouldReturnAllProducts() throws Exception {
        when(productService.getAll()).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Burger"));
    }

    @Test
    @WithMockUser
    void shouldReturnAvailableProducts() throws Exception {
        when(productService.getAvailable()).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/products/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    @WithMockUser
    void shouldReturnProductsByCategory() throws Exception {
        when(productService.getByCategory(1L)).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/products/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].category_id").value(1L))
                .andExpect(jsonPath("$[0].category_name").value("Burgers"));
    }

    @Test
    @WithMockUser
    void shouldReturnProductById() throws Exception {
        when(productService.getById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Burger"))
                .andExpect(jsonPath("$.price").value(8.50));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateProductForAdmin() throws Exception {
        when(productService.create(any())).thenReturn(mockResponse);

        String body = """
                {
                    "name": "Burger",
                    "description": "Tasty burger",
                    "price": 8.50,
                    "available": true,
                    "category_id": 1
                }
                """;

        mockMvc.perform(post("/api/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Burger"));

        verify(productService).create(any());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenWhenCustomerCreatesProduct() throws Exception {
        String body = """
                {
                    "name": "Burger",
                    "description": "Tasty burger",
                    "price": 8.50,
                    "available": true,
                    "category_id": 1
                }
                """;

        mockMvc.perform(post("/api/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());

        verifyNoInteractions(productService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProductForAdmin() throws Exception {
        when(productService.update(eq(1L), any())).thenReturn(mockResponse);

        String body = """
                {
                    "name": "Updated Burger",
                    "description": "Even tastier",
                    "price": 9.00,
                    "available": true,
                    "category_id": 1
                }
                """;

        mockMvc.perform(put("/api/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(productService).update(eq(1L), any());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenWhenCustomerUpdatesProduct() throws Exception {
        String body = """
                {
                    "name": "Updated Burger",
                    "description": "Even tastier",
                    "price": 9.00,
                    "available": true,
                    "category_id": 1
                }
                """;

        mockMvc.perform(put("/api/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());

        verifyNoInteractions(productService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteProductForAdmin() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete("/api/products/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(productService).delete(1L);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenWhenCustomerDeletesProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1").with(csrf()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(productService);
    }
}

