package com.ironhack.restoranmanagementsystem.controller;
import com.ironhack.restoranmanagementsystem.dto.request.LoginRequest;
import com.ironhack.restoranmanagementsystem.dto.request.RegisterRequest;
import com.ironhack.restoranmanagementsystem.dto.response.AuthResponse;
import com.ironhack.restoranmanagementsystem.entity.User;
import com.ironhack.restoranmanagementsystem.security.JwtTokenProvider;
import com.ironhack.restoranmanagementsystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);

        String token = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );

        User user = authService.getByEmail(request.getEmail());

        String token = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return ResponseEntity.ok(response);
    }
}

