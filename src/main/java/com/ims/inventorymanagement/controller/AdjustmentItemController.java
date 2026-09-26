package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.AdjustmentItem;
import com.ims.inventorymanagement.service.AdjustmentItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/adjustment-items")
public class AdjustmentItemController {

    private final AdjustmentItemService adjustmentItemService;

    public AdjustmentItemController(
            AdjustmentItemService adjustmentItemService
    ) {
        this.adjustmentItemService = adjustmentItemService;
    }

    @PostMapping
    public AdjustmentItem createAdjustmentItem(
            @RequestBody AdjustmentItem adjustmentItem
    ) {
        return adjustmentItemService.createAdjustmentItem(adjustmentItem);
    }

    @GetMapping
    public List<AdjustmentItem> getAllAdjustmentItems() {
        return adjustmentItemService.getAllAdjustmentItems();
    }

    @GetMapping("/{id}")
    public Optional<AdjustmentItem> getAdjustmentItemById(
            @PathVariable Long id
    ) {
        return adjustmentItemService.getAdjustmentItemById(id);
    }

    @GetMapping("/adjustment/{adjustmentId}")
    public List<AdjustmentItem> getItemsByAdjustmentId(
            @PathVariable Long adjustmentId
    ) {
        return adjustmentItemService.getItemsByAdjustmentId(adjustmentId);
    }
}
