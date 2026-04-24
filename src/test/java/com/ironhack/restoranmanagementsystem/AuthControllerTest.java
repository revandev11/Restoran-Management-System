package com.ironhack.restoranmanagementsystem;

import com.ironhack.restoranmanagementsystem.config.SecurityConfig;
import com.ironhack.restoranmanagementsystem.controller.AuthController;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.RoleName;
import com.ironhack.restoranmanagementsystem.security.JwtTokenProvider;
import com.ironhack.restoranmanagementsystem.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void shouldRegisterSuccessfully() throws Exception {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setRole(RoleName.CUSTOMER);

        when(authService.register(any())).thenReturn(user);
        when(jwtTokenProvider.generateToken("test@mail.com", "CUSTOMER"))
                .thenReturn("mock-token");

        String requestBody = """
                {
                    "fullName": "Test User",
                    "email": "test@mail.com",
                    "password": "1234",
                    "phoneNumber": "123456789"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("mock-token"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));

        verify(authService).register(any());
        verify(jwtTokenProvider).generateToken("test@mail.com", "CUSTOMER");
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        when(authService.register(any()))
                .thenThrow(new com.ironhack.restoranmanagementsystem.exception.ConflictException(
                        "User already exists"));

        String requestBody = """
                {
                    "fullName": "Test User",
                    "email": "test@mail.com",
                    "password": "1234",
                    "phoneNumber": "123456789"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setRole(RoleName.CUSTOMER);

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(authService.getByEmail("test@mail.com")).thenReturn(user);
        when(jwtTokenProvider.generateToken("test@mail.com", "CUSTOMER"))
                .thenReturn("mock-token");

        String requestBody = """
                {
                    "email": "test@mail.com",
                    "password": "1234"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));

        verify(authenticationManager).authenticate(any());
        verify(authService).getByEmail("test@mail.com");
        verify(jwtTokenProvider).generateToken("test@mail.com", "CUSTOMER");
    }

    @Test
    void shouldReturnUnauthorizedWhenBadCredentials() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        String requestBody = """
                {
                    "email": "test@mail.com",
                    "password": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().is4xxClientError());
    }
}
