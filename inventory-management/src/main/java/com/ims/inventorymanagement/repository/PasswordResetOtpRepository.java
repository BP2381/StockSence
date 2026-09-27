package com.ims.inventorymanagement.repository;

import com.ims.inventorymanagement.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepository
        extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(
            Long userId
    );
}