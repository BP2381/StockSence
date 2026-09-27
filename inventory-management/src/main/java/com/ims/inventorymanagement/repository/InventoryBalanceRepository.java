package com.ims.inventorymanagement.repository;

import com.ims.inventorymanagement.entity.InventoryBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryBalanceRepository extends JpaRepository<InventoryBalance, Long> {

    Optional<InventoryBalance> findByProductIdAndLocationId(Long productId, Long locationId);
}