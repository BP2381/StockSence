package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.InventoryBalance;
import com.ims.inventorymanagement.repository.InventoryBalanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryBalanceService {

    private final InventoryBalanceRepository inventoryBalanceRepository;

    public InventoryBalanceService(InventoryBalanceRepository inventoryBalanceRepository) {
        this.inventoryBalanceRepository = inventoryBalanceRepository;
    }

    public InventoryBalance createInventoryBalance(InventoryBalance inventoryBalance) {
        return inventoryBalanceRepository.save(inventoryBalance);
    }

    public Optional<InventoryBalance> getInventoryBalanceById(Long id) {
        return inventoryBalanceRepository.findById(id);
    }

    public List<InventoryBalance> getAllInventoryBalances() {
        return inventoryBalanceRepository.findAll();
    }

    public Optional<InventoryBalance> getBalanceByProductAndLocation(
            Long productId,
            Long locationId
    ) {
        return inventoryBalanceRepository.findByProductIdAndLocationId(productId, locationId);
    }
}