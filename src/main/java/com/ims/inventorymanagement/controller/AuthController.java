package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.dto.AuthResponse;
import com.ims.inventorymanagement.dto.LoginRequest;
import com.ims.inventorymanagement.dto.SignupRequest;
import com.ims.inventorymanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public AuthResponse signup(
            @Valid @RequestBody SignupRequest request
    ) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }
}