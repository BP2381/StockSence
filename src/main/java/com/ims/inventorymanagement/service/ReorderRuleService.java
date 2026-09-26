package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.ReorderRule;
import com.ims.inventorymanagement.repository.ReorderRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReorderRuleService {

    private final ReorderRuleRepository reorderRuleRepository;

    public ReorderRuleService(ReorderRuleRepository reorderRuleRepository) {
        this.reorderRuleRepository = reorderRuleRepository;
    }

    public ReorderRule createReorderRule(ReorderRule reorderRule) {
        return reorderRuleRepository.save(reorderRule);
    }

    public Optional<ReorderRule> getReorderRuleById(Long id) {
        return reorderRuleRepository.findById(id);
    }

    public List<ReorderRule> getAllReorderRules() {
        return reorderRuleRepository.findAll();
    }

    public Optional<ReorderRule> getRuleByProductAndLocation(
            Long productId,
            Long locationId
    ) {
        return reorderRuleRepository.findByProductIdAndLocationId(
                productId,
                locationId
        );
    }

    public List<ReorderRule> getRulesByProductId(Long productId) {
        return reorderRuleRepository.findByProductId(productId);
    }

    public List<ReorderRule> getRulesByLocationId(Long locationId) {
        return reorderRuleRepository.findByLocationId(locationId);
    }
}
