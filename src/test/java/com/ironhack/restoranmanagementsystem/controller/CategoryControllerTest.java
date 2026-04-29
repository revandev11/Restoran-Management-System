package com.ironhack.restoranmanagementsystem.controller;
import com.ironhack.restoranmanagementsystem.config.SecurityConfig;
import com.ironhack.restoranmanagementsystem.dto.response.CategoryResponse;
import com.ironhack.restoranmanagementsystem.exception.CustomAccessDeniedHandler;
import com.ironhack.restoranmanagementsystem.exception.CustomAuthenticationEntryPoint;
import com.ironhack.restoranmanagementsystem.security.JwtTokenProvider;
import com.ironhack.restoranmanagementsystem.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@Import({SecurityConfig.class, CustomAuthenticationEntryPoint.class, CustomAccessDeniedHandler.class})
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private CategoryResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = new CategoryResponse(1L, "Burgers");
    }

    @Test
    @WithMockUser
    void shouldReturnAllCategories() throws Exception {
        when(categoryService.getAll()).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Burgers"));
    }

    @Test
    @WithMockUser
    void shouldReturnCategoryById() throws Exception {
        when(categoryService.getById(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Burgers"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateCategoryForAdmin() throws Exception {
        when(categoryService.create(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Burgers\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Burgers"));

        verify(categoryService).create(any());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenWhenCustomerCreatesCategory() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Burgers\"}"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCategoryForAdmin() throws Exception {
        when(categoryService.update(eq(1L), any())).thenReturn(mockResponse);

        mockMvc.perform(put("/api/categories/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Pizzas\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(categoryService).update(eq(1L), any());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenWhenCustomerUpdatesCategory() throws Exception {
        mockMvc.perform(put("/api/categories/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Pizzas\"}"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(categoryService);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCategoryForAdmin() throws Exception {
        doNothing().when(categoryService).delete(1L);

        mockMvc.perform(delete("/api/categories/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(1L);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void shouldReturnForbiddenWhenCustomerDeletesCategory() throws Exception {
        mockMvc.perform(delete("/api/categories/1").with(csrf()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(categoryService);
    }
}

