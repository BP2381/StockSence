package com.ims.inventorymanagement.repository;

import com.ims.inventorymanagement.entity.ReorderRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReorderRuleRepository extends JpaRepository<ReorderRule, Long> {

    Optional<ReorderRule> findByProductIdAndLocationId(
            Long productId,
            Long locationId
    );

    List<ReorderRule> findByProductId(Long productId);

    List<ReorderRule> findByLocationId(Long locationId);
}