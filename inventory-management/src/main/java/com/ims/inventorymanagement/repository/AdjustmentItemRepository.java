package com.ims.inventorymanagement.repository;

import com.ims.inventorymanagement.entity.AdjustmentItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdjustmentItemRepository extends JpaRepository<AdjustmentItem, Long> {

    List<AdjustmentItem> findByAdjustmentId(Long adjustmentId);
}