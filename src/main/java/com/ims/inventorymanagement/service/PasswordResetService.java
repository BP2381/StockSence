package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.dto.ResetPasswordRequest;
import com.ims.inventorymanagement.dto.VerifyOtpRequest;
import com.ims.inventorymanagement.entity.PasswordResetOtp;
import com.ims.inventorymanagement.entity.User;
import com.ims.inventorymanagement.repository.PasswordResetOtpRepository;
import com.ims.inventorymanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordResetService(
            UserRepository userRepository,
            PasswordResetOtpRepository passwordResetOtpRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordResetOtpRepository = passwordResetOtpRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String generateOtp(String email) {

        String normalizedEmail = email.trim().toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new RuntimeException("User account is inactive");
        }

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        PasswordResetOtp passwordResetOtp = new PasswordResetOtp(
                user,
                otp,
                LocalDateTime.now().plusMinutes(10)
        );

        passwordResetOtpRepository.save(passwordResetOtp);

        return otp;
    }

    public String verifyOtp(VerifyOtpRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordResetOtp passwordResetOtp =
                passwordResetOtpRepository
                        .findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(
                                user.getId()
                        )
                        .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (passwordResetOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if (!passwordResetOtp.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        return "OTP verified successfully";
    }

    @Transactional
    public String resetPassword(ResetPasswordRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PasswordResetOtp passwordResetOtp =
                passwordResetOtpRepository
                        .findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(
                                user.getId()
                        )
                        .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (passwordResetOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if (!passwordResetOtp.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        String hashedPassword =
                passwordEncoder.encode(request.getNewPassword());

        user.setPassword(hashedPassword);
        userRepository.save(user);

        passwordResetOtp.setUsed(true);
        passwordResetOtpRepository.save(passwordResetOtp);

        return "Password reset successful";
    }
}