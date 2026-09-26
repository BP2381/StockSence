package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.StockMovement;
import com.ims.inventorymanagement.service.StockMovementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @GetMapping
    public List<StockMovement> getAllStockMovements() {
        return stockMovementService.getAllStockMovements();
    }

    @GetMapping("/{id}")
    public Optional<StockMovement> getStockMovementById(@PathVariable Long id) {
        return stockMovementService.getStockMovementById(id);
    }

    @GetMapping("/product/{productId}")
    public List<StockMovement> getMovementsByProductId(@PathVariable Long productId) {
        return stockMovementService.getMovementsByProductId(productId);
    }

    @GetMapping("/location/{locationId}")
    public List<StockMovement> getMovementsByLocationId(@PathVariable Long locationId) {
        return stockMovementService.getMovementsByLocationId(locationId);
    }
}