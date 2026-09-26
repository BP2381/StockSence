package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.InventoryBalance;
import com.ims.inventorymanagement.service.InventoryBalanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory-balances")
public class InventoryBalanceController {

    private final InventoryBalanceService inventoryBalanceService;

    public InventoryBalanceController(InventoryBalanceService inventoryBalanceService) {
        this.inventoryBalanceService = inventoryBalanceService;
    }

    @PostMapping
    public InventoryBalance createInventoryBalance(@RequestBody InventoryBalance inventoryBalance) {
        return inventoryBalanceService.createInventoryBalance(inventoryBalance);
    }

    @GetMapping
    public List<InventoryBalance> getAllInventoryBalances() {
        return inventoryBalanceService.getAllInventoryBalances();
    }

    @GetMapping("/{id}")
    public Optional<InventoryBalance> getInventoryBalanceById(@PathVariable Long id) {
        return inventoryBalanceService.getInventoryBalanceById(id);
    }

    @GetMapping("/product/{productId}/location/{locationId}")
    public Optional<InventoryBalance> getBalanceByProductAndLocation(
            @PathVariable Long productId,
            @PathVariable Long locationId
    ) {
        return inventoryBalanceService.getBalanceByProductAndLocation(productId, locationId);
    }
}