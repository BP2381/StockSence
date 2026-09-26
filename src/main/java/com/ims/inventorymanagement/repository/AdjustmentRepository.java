package com.ims.inventorymanagement.repository;

import com.ims.inventorymanagement.entity.Adjustment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {

    Optional<Adjustment> findByAdjustmentNumber(String adjustmentNumber);
}