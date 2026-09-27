package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.dto.AuthResponse;
import com.ims.inventorymanagement.dto.LoginRequest;
import com.ims.inventorymanagement.dto.SignupRequest;
import com.ims.inventorymanagement.entity.User;
import com.ims.inventorymanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse signup(SignupRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already registered");
        }

        String hashedPassword =
                passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getFullName().trim(),
                email,
                hashedPassword,
                User.UserRole.WAREHOUSE_STAFF
        );

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                token,
                "Signup successful"
        );
    }

    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new RuntimeException(
                    "User account is inactive"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                token,
                "Login successful"
        );
    }
}