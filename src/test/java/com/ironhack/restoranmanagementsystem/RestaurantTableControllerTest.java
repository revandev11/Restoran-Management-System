package com.ironhack.restoranmanagementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.restoranmanagementsystem.config.SecurityConfig;
import com.ironhack.restoranmanagementsystem.controller.RestaurantTableController;
import com.ironhack.restoranmanagementsystem.dto.request.TableCreateRequest;
import com.ironhack.restoranmanagementsystem.dto.response.TableResponse;
import com.ironhack.restoranmanagementsystem.exception.CustomAccessDeniedHandler;
import com.ironhack.restoranmanagementsystem.exception.CustomAuthenticationEntryPoint;
import com.ironhack.restoranmanagementsystem.security.JwtTokenProvider;
import com.ironhack.restoranmanagementsystem.service.RestaurantTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantTableController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class, CustomAccessDeniedHandler.class, CustomAuthenticationEntryPoint.class})
public class RestaurantTableControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private RestaurantTableService restaurantTableService;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private TableResponse tableResponse;
    private TableCreateRequest tableRequest;

    @BeforeEach
    void setUp() {
        tableResponse = new TableResponse(1L,5,4,true);
        tableRequest = new TableCreateRequest(5,4,true);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void createTable_ShouldReturnCreatedTable() throws Exception {
        Mockito.when(restaurantTableService.createTable(any(TableCreateRequest.class)))
                .thenReturn(tableResponse);

        mockMvc.perform(post("/api/restaurant")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tableRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.table_number").value(5));
    }
    @Test
    @WithMockUser(roles = "USER")
    void createTable_ShouldReturnForbiddenForNonAdmin() throws Exception {
        mockMvc.perform(post("/api/restaurant")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tableRequest)))
                .andExpect(status().isForbidden());
    }
    @Test
    void findAllByAvailable_ShouldReturnList() throws Exception {
        Mockito.when(restaurantTableService.findAllAvailables(true))
                .thenReturn(List.of(tableResponse));

        mockMvc.perform(get("/api/restaurant/status")
                        .param("status", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].available").value(true));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTable_ShouldReturnOk() throws Exception {
        Mockito.doNothing().when(restaurantTableService).deleteTable(1L);

        mockMvc.perform(delete("/api/restaurant/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
        Mockito.verify(restaurantTableService, Mockito.times(1)).deleteTable(1L);
    }
    @Test
    void getTablesByMinCapacity_ShouldReturnFilteredTables() throws Exception {
        Mockito.when(restaurantTableService.getTablesByMinCapacity(4))
                .thenReturn(List.of(tableResponse));
        mockMvc.perform(get("/api/restaurant/capacity")
                        .param("minCapacity", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].capacity").value(4));
    }
    @Test
    void findByTableNumber_ShouldReturnTable() throws Exception {
        Mockito.when(restaurantTableService.findTableNumber(5))
                .thenReturn(tableResponse);
        mockMvc.perform(get("/api/restaurant/number/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.table_number").value(5));
    }
}