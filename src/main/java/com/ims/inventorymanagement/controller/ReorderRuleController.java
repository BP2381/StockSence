package com.ims.inventorymanagement.controller;

import com.ims.inventorymanagement.entity.ReorderRule;
import com.ims.inventorymanagement.service.ReorderRuleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reorder-rules")
public class ReorderRuleController {

    private final ReorderRuleService reorderRuleService;

    public ReorderRuleController(ReorderRuleService reorderRuleService) {
        this.reorderRuleService = reorderRuleService;
    }

    @PostMapping
    public ReorderRule createReorderRule(
            @RequestBody ReorderRule reorderRule
    ) {
        return reorderRuleService.createReorderRule(reorderRule);
    }

    @GetMapping
    public List<ReorderRule> getAllReorderRules() {
        return reorderRuleService.getAllReorderRules();
    }

    @GetMapping("/{id}")
    public Optional<ReorderRule> getReorderRuleById(
            @PathVariable Long id
    ) {
        return reorderRuleService.getReorderRuleById(id);
    }

    @GetMapping("/product/{productId}")
    public List<ReorderRule> getRulesByProductId(
            @PathVariable Long productId
    ) {
        return reorderRuleService.getRulesByProductId(productId);
    }

    @GetMapping("/location/{locationId}")
    public List<ReorderRule> getRulesByLocationId(
            @PathVariable Long locationId
    ) {
        return reorderRuleService.getRulesByLocationId(locationId);
    }

    @GetMapping("/product/{productId}/location/{locationId}")
    public Optional<ReorderRule> getRuleByProductAndLocation(
            @PathVariable Long productId,
            @PathVariable Long locationId
    ) {
        return reorderRuleService.getRuleByProductAndLocation(
                productId,
                locationId
        );
    }
}
