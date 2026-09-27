package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.entity.Adjustment;
import com.ims.inventorymanagement.entity.AdjustmentItem;
import com.ims.inventorymanagement.entity.InventoryBalance;
import com.ims.inventorymanagement.entity.StockMovement;
import com.ims.inventorymanagement.repository.AdjustmentItemRepository;
import com.ims.inventorymanagement.repository.AdjustmentRepository;
import com.ims.inventorymanagement.repository.InventoryBalanceRepository;
import com.ims.inventorymanagement.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AdjustmentService {

    private final AdjustmentRepository adjustmentRepository;
    private final AdjustmentItemRepository adjustmentItemRepository;
    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final StockMovementRepository stockMovementRepository;

    public AdjustmentService(
            AdjustmentRepository adjustmentRepository,
            AdjustmentItemRepository adjustmentItemRepository,
            InventoryBalanceRepository inventoryBalanceRepository,
            StockMovementRepository stockMovementRepository
    ) {
        this.adjustmentRepository = adjustmentRepository;
        this.adjustmentItemRepository = adjustmentItemRepository;
        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public Adjustment createAdjustment(Adjustment adjustment) {
        return adjustmentRepository.save(adjustment);
    }

    @Transactional
    public Adjustment validateAdjustment(Long adjustmentId) {

        Adjustment adjustment = adjustmentRepository.findById(adjustmentId)
                .orElseThrow(() -> new RuntimeException(
                        "Adjustment not found"
                ));

        if (adjustment.getStatus() == Adjustment.AdjustmentStatus.DONE) {
            throw new RuntimeException(
                    "Adjustment is already validated"
            );
        }

        if (adjustment.getStatus() == Adjustment.AdjustmentStatus.CANCELED) {
            throw new RuntimeException(
                    "Canceled adjustment cannot be validated"
            );
        }

        List<AdjustmentItem> items =
                adjustmentItemRepository.findByAdjustmentId(adjustmentId);

        if (items.isEmpty()) {
            throw new RuntimeException(
                    "Adjustment has no items"
            );
        }

        for (AdjustmentItem item : items) {

            if (item.getCountedQuantity() == null ||
                    item.getCountedQuantity().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException(
                        "Counted quantity cannot be negative"
                );
            }

            InventoryBalance balance =
                    inventoryBalanceRepository
                            .findByProductIdAndLocationId(
                                    item.getProduct().getId(),
                                    item.getLocation().getId()
                            )
                            .orElseGet(() -> new InventoryBalance(
                                    item.getProduct(),
                                    item.getLocation(),
                                    BigDecimal.ZERO
                            ));

            BigDecimal currentQuantity = balance.getQuantity();

            BigDecimal difference =
                    item.getCountedQuantity().subtract(currentQuantity);

            if (difference.compareTo(BigDecimal.ZERO) != 0) {

                StockMovement movement = new StockMovement(
                        item.getProduct(),
                        item.getLocation(),
                        StockMovement.MovementType.ADJUSTMENT,
                        difference.abs(),
                        "ADJUSTMENT",
                        adjustment.getId(),
                        "Adjustment " +
                                adjustment.getAdjustmentNumber() +
                                " (difference: " +
                                difference +
                                ")"
                );

                stockMovementRepository.save(movement);
            }

            balance.setQuantity(item.getCountedQuantity());

            inventoryBalanceRepository.save(balance);
        }

        adjustment.setStatus(Adjustment.AdjustmentStatus.DONE);

        return adjustmentRepository.save(adjustment);
    }

    public Optional<Adjustment> getAdjustmentById(Long id) {
        return adjustmentRepository.findById(id);
    }

    public List<Adjustment> getAllAdjustments() {
        return adjustmentRepository.findAll();
    }

    public Optional<Adjustment> getAdjustmentByNumber(
            String adjustmentNumber
    ) {
        return adjustmentRepository
                .findByAdjustmentNumber(adjustmentNumber);
    }
}
