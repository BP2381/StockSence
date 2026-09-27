package com.ims.inventorymanagement.service;

import com.ims.inventorymanagement.dto.StockMovementSummary;
import com.ims.inventorymanagement.entity.StockMovement;
import com.ims.inventorymanagement.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;

    public StockMovementService(
            StockMovementRepository stockMovementRepository
    ) {
        this.stockMovementRepository = stockMovementRepository;
    }

    public StockMovement createStockMovement(
            StockMovement stockMovement
    ) {
        return stockMovementRepository.save(stockMovement);
    }

    public Optional<StockMovement> getStockMovementById(Long id) {
        return stockMovementRepository.findById(id);
    }

    public List<StockMovement> getAllStockMovements() {
        return stockMovementRepository.findAll();
    }

    public List<StockMovement> getMovementsByProductId(
            Long productId
    ) {
        return stockMovementRepository.findByProductId(productId);
    }

    public List<StockMovement> getMovementsByLocationId(
            Long locationId
    ) {
        return stockMovementRepository.findByLocationId(locationId);
    }

    public List<StockMovementSummary> getMovementSummaries() {

        return stockMovementRepository.findAll()
                .stream()
                .map(movement -> new StockMovementSummary(
                        movement.getId(),
                        movement.getProduct().getName(),
                        movement.getProduct().getSku(),
                        movement.getLocation().getName(),
                        movement.getMovementType(),
                        movement.getQuantity(),
                        movement.getReferenceType(),
                        movement.getReferenceId(),
                        movement.getNotes(),
                        movement.getCreatedAt()
                ))
                .toList();
    }
}