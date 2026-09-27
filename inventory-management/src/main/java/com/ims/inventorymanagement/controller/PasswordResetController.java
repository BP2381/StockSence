package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.dto.ForgotPasswordRequest;
import com.ims.inventorymanagement.dto.ResetPasswordRequest;
import com.ims.inventorymanagement.dto.VerifyOtpRequest;
import com.ims.inventorymanagement.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password-reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(
            PasswordResetService passwordResetService
    ) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/request")
    public String requestOtp(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        return passwordResetService.generateOtp(request.getEmail());
    }

    @PostMapping("/verify")
    public String verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ) {
        return passwordResetService.verifyOtp(request);
    }

    @PostMapping("/reset")
    public String resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        return passwordResetService.resetPassword(request);
    }
}