package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.Adjustment;
import com.ims.inventorymanagement.service.AdjustmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/adjustments")
public class AdjustmentController {

    private final AdjustmentService adjustmentService;

    public AdjustmentController(AdjustmentService adjustmentService) {
        this.adjustmentService = adjustmentService;
    }

    @PostMapping
    public Adjustment createAdjustment(
            @RequestBody Adjustment adjustment
    ) {
        return adjustmentService.createAdjustment(adjustment);
    }

    @GetMapping
    public List<Adjustment> getAllAdjustments() {
        return adjustmentService.getAllAdjustments();
    }

    @GetMapping("/{id}")
    public Optional<Adjustment> getAdjustmentById(
            @PathVariable Long id
    ) {
        return adjustmentService.getAdjustmentById(id);
    }

    @PutMapping("/{id}/validate")
    public Adjustment validateAdjustment(
            @PathVariable Long id
    ) {
        return adjustmentService.validateAdjustment(id);
    }
}