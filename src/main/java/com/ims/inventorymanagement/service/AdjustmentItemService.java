package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.AdjustmentItem;
import com.ims.inventorymanagement.repository.AdjustmentItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdjustmentItemService {

    private final AdjustmentItemRepository adjustmentItemRepository;

    public AdjustmentItemService(
            AdjustmentItemRepository adjustmentItemRepository
    ) {
        this.adjustmentItemRepository = adjustmentItemRepository;
    }

    public AdjustmentItem createAdjustmentItem(
            AdjustmentItem adjustmentItem
    ) {
        return adjustmentItemRepository.save(adjustmentItem);
    }

    public Optional<AdjustmentItem> getAdjustmentItemById(Long id) {
        return adjustmentItemRepository.findById(id);
    }

    public List<AdjustmentItem> getAllAdjustmentItems() {
        return adjustmentItemRepository.findAll();
    }

    public List<AdjustmentItem> getItemsByAdjustmentId(Long adjustmentId) {
        return adjustmentItemRepository.findByAdjustmentId(adjustmentId);
    }
}
