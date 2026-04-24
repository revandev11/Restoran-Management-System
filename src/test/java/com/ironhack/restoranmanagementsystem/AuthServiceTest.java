package com.ironhack.restoranmanagementsystem;

import com.ironhack.restoranmanagementsystem.dto.request.RegisterRequest;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.enums.RoleName;
import com.ironhack.restoranmanagementsystem.exception.ConflictException;
import com.ironhack.restoranmanagementsystem.repository.UserRepository;
import com.ironhack.restoranmanagementsystem.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Test User");
        request.setEmail("test@mail.com");
        request.setPassword("123456");
        request.setPhoneNumber("123456789");
        request.setRole("customer");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded_pass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = authService.register(request);

        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
        assertEquals("Test User", result.getFullName());
        assertEquals("encoded_pass", result.getPassword());
        assertEquals(RoleName.CUSTOMER, result.getRole());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void shouldThrowConflictWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.com");

        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(ConflictException.class, () ->
                authService.register(request)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldSetDefaultRoleWhenRoleIsNull() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@mail.com");
        request.setPassword("123456");
        request.setRole(null);

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = authService.register(request);

        assertEquals(RoleName.CUSTOMER, result.getRole());
    }

    @Test
    void shouldSetAdminRoleWhenRoleIsAdmin() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("admin@mail.com");
        request.setPassword("123456");
        request.setRole("admin");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        User result = authService.register(request);

        assertEquals(RoleName.ADMIN, result.getRole());
    }

    @Test
    void shouldReturnUserByEmail() {
        User user = new User();
        user.setEmail("test@mail.com");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        User result = authService.getByEmail("test@mail.com");

        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
        verify(userRepository).findByEmail("test@mail.com");
    }

    @Test
    void shouldThrowWhenUserNotFoundByEmail() {
        when(userRepository.findByEmail("notfound@mail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                authService.getByEmail("notfound@mail.com")
        );
    }
}
